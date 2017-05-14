package ru.magicwolf.API_interfases;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.magicwolf.POJO_classes.DrugResp;


public interface GetDrugsAPI {
    @GET("GetDrugsServlet")
    Call<DrugResp> GetDrugs(@Query("name") String username,
                     @Query("password") String password);
}
