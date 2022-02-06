package de.challenge.di

import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import de.challenge.api.network.NetworkResponseMapper
import de.challenge.api.network.ResponseMapper
import de.challenge.api.network.VehicleApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        const val BASE_URL = "https://api.jsonbin.io"
    }

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()

    @Provides
    @Singleton
    @IntoSet
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    @Singleton
    fun provideMoshi() : Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(
        moshi: Moshi,
        interceptors: @JvmSuppressWildcards Set<Interceptor>,
        httpClientBuilder: OkHttpClient.Builder
    ): Retrofit {
        return Retrofit.Builder()
            .client(httpClientBuilder.apply { interceptors.forEach(this::addInterceptor) }
                .build())
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @IntoSet
    fun provideHeaderInterceptor(): Interceptor = Interceptor { chain ->
        val builder = chain.request().newBuilder()
        builder.header("Content-Type", "application/json")
        // this 'secret' should be in real life read/stored/retrieved from some protected place and not to be hardcoded
        builder.header(
            "secret-key",
            "\$2b\$10\$VE0tRqquld4OBl7LDeo9v.afsyRXFlXcQzmj1KpEB6K1wG2okzQcK"
        )
        return@Interceptor chain.proceed(builder.build())
    }

    @Provides
    @Singleton
    internal fun provideVehicleApi(retrofit: Retrofit): VehicleApi {
        return retrofit.create(VehicleApi::class.java)
    }

    @Provides
    @Singleton
    internal fun bindResponseMapper(): ResponseMapper = NetworkResponseMapper()
}