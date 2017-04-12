package com.tine.coffeeshops;

import com.github.simonpercic.oklog3.OkLogInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tine.coffeeshops.api.service.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    @Provides @Singleton HttpUrl provideApiUrl() {
        return HttpUrl.parse(Constants.PLACES_API_URL);
    }

    @Provides @Singleton Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides @Singleton Converter.Factory provideConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides @Singleton OkHttpClient provideOkHttpClient(OkLogInterceptor interceptor) {
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    @Provides @Singleton Retrofit provideRetrofit(OkHttpClient client, HttpUrl baseUrl, Converter.Factory converter,
            RxJavaCallAdapterFactory callAdapterFactory) {
        return getRetrofit(client, baseUrl, converter, callAdapterFactory);
    }

   @Provides @Singleton OkLogInterceptor provideOkLogInterceptor(){
       return OkLogInterceptor.builder().build();
   }

    @Provides @Singleton ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides @Singleton RxJavaCallAdapterFactory provideRxCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }

    private static Retrofit getRetrofit(OkHttpClient client, HttpUrl url, Converter.Factory converter,
            RxJavaCallAdapterFactory callAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(converter)
                .addCallAdapterFactory(callAdapterFactory)
                .build();
    }
}
