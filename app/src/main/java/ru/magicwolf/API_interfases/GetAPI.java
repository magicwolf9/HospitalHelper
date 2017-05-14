package ru.magicwolf.API_interfases;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.magicwolf.POJO_classes.Resp;


public interface GetAPI {
    @GET("LoginServlet")
    Call<Resp> Login(@Query("name") String username,
                     @Query("password") String password);
}

