package com.example.quacksports.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quacksports.data.logic.Revenue
import com.example.quacksports.data.repository.AuthRepository
import com.example.quacksports.data.repository.CompanyRepository
import com.example.quacksports.data.repository.PaymentRepository
import com.example.quacksports.data.repository.ReservationRepository
import com.example.quacksports.data.repository.VenueRepository
import com.example.quacksports.model.Company
import com.example.quacksports.model.Payment
import com.example.quacksports.model.Reservation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class AdminViewModel(
    private val authRepo: AuthRepository = AuthRepository(),
    private val companyRepo: CompanyRepository = CompanyRepository(),
    private val reservationRepo: ReservationRepository = ReservationRepository(),
    private val venueRepo: VenueRepository = VenueRepository(),
    private val paymentRepo: PaymentRepository = PaymentRepository()
) : ViewModel() {
    private val _companies = MutableStateFlow<List<Company>>(emptyList())
    val companies: StateFlow<List<Company>> = _companies
    private val _reservations = MutableStateFlow<List<Reservation>>(emptyList())
    val reservations: StateFlow<List<Reservation>> = _reservations
    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val payments: StateFlow<List<Payment>> = _payments
    private val _venueCount = MutableStateFlow(0)
    val venueCount: StateFlow<Int> = _venueCount
    private val _totalRevenue = MutableStateFlow(0.0)
    val totalRevenue: StateFlow<Double> = _totalRevenue
    private val _revenuePerCompany = MutableStateFlow<Map<String, Double>>(emptyMap())
    val revenuePerCompany: StateFlow<Map<String, Double>> = _revenuePerCompany
    private val _isCreatingCompany = MutableStateFlow(false)
    val isCreatingCompany: StateFlow<Boolean> = _isCreatingCompany
    private val _companyMessage = MutableStateFlow<String?>(null)
    val companyMessage: StateFlow<String?> = _companyMessage

    private val _cnpjData = MutableStateFlow<Map<String, String>?>(null)
    val cnpjData: StateFlow<Map<String, String>?> = _cnpjData

    private val _isSearchingCnpj = MutableStateFlow(false)
    val isSearchingCnpj: StateFlow<Boolean> = _isSearchingCnpj

    init {
        viewModelScope.launch { companyRepo.all().collect { _companies.value = it } }
        viewModelScope.launch { venueRepo.allVenues().collect { _venueCount.value = it.size } }
        viewModelScope.launch {
            reservationRepo.all().collect { list ->
                _reservations.value = list.sortedByDescending { it.createdAt }
                _revenuePerCompany.value = Revenue.perCompany(list)
            }
        }
        refreshPayments()
    }

    fun refreshPayments() = viewModelScope.launch {
        val p = paymentRepo.all()
        _payments.value = p.sortedByDescending { it.createdAt }
        _totalRevenue.value = Revenue.total(p)
    }

    fun createCompany(context: Context, name: String, email: String, logoUrl: String, description: String) =
        viewModelScope.launch {
            if (_isCreatingCompany.value) return@launch
            val cleanName = name.trim()
            val cleanEmail = email.trim().lowercase()
            if (cleanName.isBlank() || cleanEmail.isBlank()) {
                _companyMessage.value = "Informe nome e email da empresa"
                return@launch
            }

            _isCreatingCompany.value = true
            _companyMessage.value = null
            try {
                val ownerUid = authRepo.createCompanyOwnerAccount(
                    context.applicationContext,
                    cleanEmail,
                    AuthRepository.COMPANY_DEFAULT_PASSWORD
                )
                val companyId = companyRepo.upsert(
                    Company(
                        name = cleanName,
                        ownerUid = ownerUid,
                        email = cleanEmail,
                        logoUrl = logoUrl.trim(),
                        description = description.trim(),
                        createdAt = System.currentTimeMillis()
                    )
                )
                authRepo.upsertCompanyOwnerUser(ownerUid, cleanName, cleanEmail, companyId)
                _companyMessage.value = "Empresa cadastrada. Login: $cleanEmail / senha 123456"
            } catch (e: Exception) {
                _companyMessage.value = "Erro ao cadastrar empresa: ${e.message}"
            } finally {
                _isCreatingCompany.value = false
            }
        }

    fun clearCompanyMessage() {
        _companyMessage.value = null
    }

    fun searchCnpj(cnpj: String) = viewModelScope.launch {
        val cleanCnpj = cnpj.replace(Regex("[^0-9]"), "")
        if (cleanCnpj.length != 14) {
            _companyMessage.value = "CNPJ inválido (deve ter 14 dígitos)"
            return@launch
        }

        _isSearchingCnpj.value = true
        _cnpjData.value = null
        try {
            val result = withContext(Dispatchers.IO) {
                val url = URL("https://brasilapi.com.br/api/cnpj/v1/$cleanCnpj")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.connectTimeout = 5000
                conn.readTimeout = 5000
                
                if (conn.responseCode == 200) {
                    val text = conn.inputStream.bufferedReader().use { it.readText() }
                    val json = JSONObject(text)
                    mapOf(
                        "name" to (json.optString("nome_fantasia").ifBlank { json.optString("razao_social") }),
                        "description" to "CNPJ: $cleanCnpj - ${json.optString("razao_social")}"
                    )
                } else {
                    null
                }
            }

            if (result != null) {
                _cnpjData.value = result
            } else {
                _companyMessage.value = "CNPJ não encontrado ou erro na API"
            }
        } catch (e: Exception) {
            _companyMessage.value = "Erro ao buscar CNPJ: ${e.message}"
        } finally {
            _isSearchingCnpj.value = false
        }
    }

    fun clearCnpjData() {
        _cnpjData.value = null
    }
}
