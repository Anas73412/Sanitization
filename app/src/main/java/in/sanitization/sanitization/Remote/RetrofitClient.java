package in.sanitization.sanitization.Remote;

import in.sanitization.sanitization.Config.BaseUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static in.sanitization.sanitization.Config.BaseUrl.BASE_URL;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 20,January,2020
 */
public class RetrofitClient {

    private static Retrofit retrofitClient=null;
    public static Retrofit getClient(String baseUrl)
    {
        if(retrofitClient ==null)
        {
            retrofitClient=new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofitClient;
    }
    public static Retrofit getRetrofitInstance() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if (retrofitClient == null) {
            retrofitClient = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofitClient;
    }
}
