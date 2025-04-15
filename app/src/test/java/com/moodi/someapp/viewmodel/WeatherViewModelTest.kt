import com.moodi.someapp.data.util.Resource
import com.moodi.someapp.domain.model.WeatherAppData
import com.moodi.someapp.domain.model.WeatherCondition
import com.moodi.someapp.domain.model.WeatherUnit
import com.moodi.someapp.domain.repository.WeatherRepository
import com.moodi.someapp.rule.TestDispatcherRule
import com.moodi.someapp.viewmodel.UIEvent
import com.moodi.someapp.viewmodel.WeatherViewModel
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
        assertFalse(viewModel.state.loading)
        assertNull(viewModel.state.weatherData?.temperature)
        assertNull(viewModel.state.weatherData?.unit)
        assertNull(viewModel.state.weatherData?.condition)
        assertNull(viewModel.state.error)
    }

    @Test
    fun `fetching weather updates state with loading`() = runTest {
        // Given
        val loadingFlow = flowOf(Resource.Loading<WeatherAppData>())
        every { weatherRepository.getWeather(any(), any()) } returns loadingFlow

        // When
        viewModel.sendEvent(UIEvent.FetchWeather(testLat, testLng))

        // Then
        assertTrue(viewModel.state.loading)
        verify { weatherRepository.getWeather(testLat, testLng) }
    }

    @Test
    fun `fetching weather updates state with success`() = runTest {
        // Given
        val temperature = 25.5
        val description = WeatherCondition.Cloudy
        val unit = WeatherUnit.STANDARD
        val successFlow = flowOf(
            Resource.Success(
                WeatherAppData(
                    temperature = temperature, unit = unit, condition = description
                )
            )
        )
        every { weatherRepository.getWeather(any(), any()) } returns successFlow

        // When
        viewModel.sendEvent(UIEvent.FetchWeather(testLat, testLng))

        // Then
        assertEquals(temperature, viewModel.state.weatherData?.temperature)
        assertEquals(description, viewModel.state.weatherData?.condition)
        assertEquals(unit, viewModel.state.weatherData?.unit)
        assertFalse(viewModel.state.loading)
        assertNull(viewModel.state.error)
    }

    @Test
    fun `fetching weather updates state with error`() = runTest {
        // Given
        val errorMessage = "Something went wrong"
        val errorFlow = flowOf(Resource.Error<WeatherAppData>(errorMessage))
        every { weatherRepository.getWeather(any(), any()) } returns errorFlow

        // When
        viewModel.sendEvent(UIEvent.FetchWeather(testLat, testLng))

        // Then
        assertEquals(errorMessage, viewModel.state.error)
        assertFalse(viewModel.state.loading)
    }

    @Test
    fun `complete flow from loading to success`() = runTest {
        // Given
        val temperature = 25.5
        val description = WeatherCondition.Cloudy
        val unit = WeatherUnit.STANDARD

        val completeFlow = flowOf(
            Resource.Loading<WeatherAppData>(), Resource.Success(
                WeatherAppData(
                    temperature = temperature, condition = description, unit = unit
                )
            )
        )
        every { weatherRepository.getWeather(any(), any()) } returns completeFlow

        // When
        viewModel.sendEvent(UIEvent.FetchWeather(testLat, testLng))

        // Then
        assertEquals(temperature, viewModel.state.weatherData?.temperature)
        assertEquals(description, viewModel.state.weatherData?.condition)
        assertFalse(viewModel.state.loading)
        assertNull(viewModel.state.error)
    }

    @Test
    fun `complete flow from loading to error`() = runTest {
        // Given
        val errorMessage = "Network error"
        val completeFlow = flowOf(
            Resource.Loading<WeatherAppData>(), Resource.Error<WeatherAppData>(errorMessage)
        )
        every { weatherRepository.getWeather(any(), any()) } returns completeFlow

        // When
        viewModel.sendEvent(UIEvent.FetchWeather(testLat, testLng))

        // Then
        assertEquals(errorMessage, viewModel.state.error)
        assertFalse(viewModel.state.loading)
    }
}