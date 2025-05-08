import com.moodi.someapp.core.common.Resource
import com.moodi.someapp.domain.model.WeatherApp
import com.moodi.someapp.domain.model.WeatherCondition
import com.moodi.someapp.domain.model.WeatherUnit
import com.moodi.someapp.domain.repository.WeatherRepository
import com.moodi.someapp.presentation.viewmodel.UIEvent
import com.moodi.someapp.presentation.viewmodel.WeatherViewModel
import com.moodi.someapp.rule.TestDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    private lateinit var viewModel: WeatherViewModel
    private val weatherRepository = mockk<WeatherRepository>()

    private val testLat = 37.7749
    private val testLng = -122.4194

    @Before
    fun setup() {
        viewModel = WeatherViewModel(weatherRepository)
    }

    @Test
    fun `initial state is correct`() {
        // Assert initial state values
        assertFalse(viewModel.state.value.loading)
        assertNull(viewModel.state.value.weatherData?.temperature)
        assertEquals("Hilversum", viewModel.state.value.weatherData?.locationName)
        assertNull(viewModel.state.value.weatherData?.condition)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `fetching weather updates state with loading`() = runTest {
        // Given
        val loadingFlow = flowOf(Resource.Loading<WeatherApp>())
        every {
            weatherRepository.getWeather(
                any(), any(), WeatherUnit.STANDARD
            )
        } returns loadingFlow

        // When
        viewModel.sendEvent(UIEvent.FetchWeather(testLat, testLng, WeatherUnit.STANDARD))

        // Then
        assertTrue(viewModel.state.value.loading)
        verify { weatherRepository.getWeather(testLat, testLng, WeatherUnit.STANDARD) }
    }

    @Test
    fun `fetching weather updates state with success`() = runTest {
        // Given
        val temperature = 25.5
        val description = WeatherCondition.Cloudy
        val unit = WeatherUnit.STANDARD
        val successFlow = flowOf(
            Resource.Success(
                WeatherApp(
                    temperature = temperature,
                    locationName = "Hilversum",
                    condition = listOf(description)
                )
            )
        )
        every { weatherRepository.getWeather(any(), any(), unit) } returns successFlow

        // When
        viewModel.sendEvent(UIEvent.FetchWeather(testLat, testLng, WeatherUnit.STANDARD))

        // Then
        assertEquals(temperature, viewModel.state.value.weatherData?.temperature)
        assertEquals(description, viewModel.state.value.weatherData?.condition)
        assertEquals("Hilversum", viewModel.state.value.weatherData?.locationName)
        assertFalse(viewModel.state.value.loading)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `fetching weather updates state with error`() = runTest {
        // Given
        val errorMessage = "Something went wrong"
        val errorFlow = flowOf(Resource.Error<WeatherApp>(errorMessage))
        every { weatherRepository.getWeather(any(), any(), WeatherUnit.STANDARD) } returns errorFlow

        // When
        viewModel.sendEvent(UIEvent.FetchWeather(testLat, testLng, WeatherUnit.STANDARD))

        // Then
        assertEquals(errorMessage, viewModel.state.value.error)
        assertFalse(viewModel.state.value.loading)
    }

    @Test
    fun `complete flow from loading to success`() = runTest {
        // Given
        val temperature = 25.5
        val description = WeatherCondition.Cloudy
        val unit = WeatherUnit.STANDARD

        val completeFlow = flowOf(
            Resource.Loading<WeatherApp>(), Resource.Success(
                WeatherApp(
                    temperature = temperature,
                    condition = listOf(description),
                    locationName = "Hilversum"
                )
            )
        )
        every { weatherRepository.getWeather(any(), any(), unit) } returns completeFlow

        // When
        viewModel.sendEvent(UIEvent.FetchWeather(testLat, testLng, WeatherUnit.STANDARD))

        // Then
        assertEquals(temperature, viewModel.state.value.weatherData?.temperature)
        assertEquals(description, viewModel.state.value.weatherData?.condition)
        assertFalse(viewModel.state.value.loading)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `complete flow from loading to error`() = runTest {
        // Given
        val errorMessage = "Network error"
        val completeFlow = flowOf(
            Resource.Loading<WeatherApp>(), Resource.Error<WeatherApp>(errorMessage)
        )
        every {
            weatherRepository.getWeather(
                any(),
                any(),
                WeatherUnit.STANDARD
            )
        } returns completeFlow

        // When
        viewModel.sendEvent(UIEvent.FetchWeather(testLat, testLng, WeatherUnit.STANDARD))

        // Then
        assertEquals(errorMessage, viewModel.state.value.error)
        assertFalse(viewModel.state.value.loading)
    }
}