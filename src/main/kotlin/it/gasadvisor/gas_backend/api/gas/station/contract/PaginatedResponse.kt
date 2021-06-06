package it.gasadvisor.gas_backend.api.gas.station.contract

data class PaginatedResponse<T>(
    val currentPage: Int,
    val currentSize: Int,
    val totalElements: Long,
    val totalPages: Int,
    val values: List<T>
)
