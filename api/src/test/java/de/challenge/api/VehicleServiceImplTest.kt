package de.challenge.api

import de.challenge.api.model.*
import de.challenge.api.mappers.NetworkResponseMapper
import de.challenge.api.network.VehicleApi
import de.challenge.api.network.VehicleServiceImpl
import de.challenge.api.utils.CoroutineTestRule
import de.challenge.api.utils.MockResponseFileReader
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.Assert.assertEquals
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
class VehicleServiceImplTest {
    private val server: MockWebServer = MockWebServer()
    private val PORT = 8000
    private val vehicleList = listOf(
        VehicleResponse(
            "6348dfa0-1b20-40ed-98e9-fe9e232b6105",
            "8ece0495-bef0-4eac-a58e-dede2bf975a3",
            "BERLIN",
            "CLAIMED",
            91,
            "ACTIVE",
            "AB",
            "52.506731",
            "13.289618"
        ),
        VehicleResponse(
            "6348dfa0-1b20-40ed-98e9-fe9e232b6106",
            "8ece0495-bef0-4eac-a58e-dede2bf975a3",
            "BERLIN",
            "CLAIMED",
            91,
            "ACTIVE",
            "MOCK",
            "52.506731",
            "13.289618"
        )
    )
    private val successResponse = VehicleListResponseData(VehicleListResponse(vehicleList, null))


    private lateinit var vehicleApi: VehicleApi
    private lateinit var vehicleService: VehicleServiceImpl

    @get:Rule
    val rule = CoroutineTestRule()

    @Before
    fun init() {
        server.start(PORT)
        vehicleApi = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(VehicleApi::class.java)
        vehicleService = VehicleServiceImpl(vehicleApi, NetworkResponseMapper())
    }

    @Test
    fun shouldReturnMappedResponse() = runTest {
       val body = MockResponseFileReader("/mock_response.json").content
        server.apply {
            enqueue(MockResponse().setBody(body))
        }
       val response =  vehicleService.getVehicles()
        assert(response.isSuccess())
        assertEquals((response as ApiResult.Success).result, successResponse)
    }

    /**
     * more tests to come...
     */

    @After
    fun shutdown() {
        server.shutdown()
    }
}