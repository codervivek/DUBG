package com.univ.team12.navar.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Amal Krishnan on 06-03-2017.
 */

public interface RetrofitInterface {

    @GET("maps/api/directions/json?")
    Call<DirectionsResponse> getDirections(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String key
    );
    @GET("location/update?")
    Call<DirectionsResponse> updateStatus(
            @Query("pk") String pk,
            @Query("latitude") String latitude,
            @Query("longitude") String longtitude
    );

    @GET("message/create?")
    Call<DirectionsResponse> createMessage(
            @Query("pk") String pk,
            @Query("latitude") String latitude,
            @Query("longitude") String longtitude,
            @Query("message") String message
    );

    @GET("status/create?")
    Call<DirectionsResponse> createStatus(
            @Query("name") String name,
            @Query("latitude") String latitude,
            @Query("longitude") String longtitude,
            @Query("people_stuck") String people_stuck,
            @Query("people_injured") String people_injured
            );

    @GET("a")
    Call<PoiResponse> listPOI();

    @GET("message?")
    Call<MessageResponse> listMessage(
            @Query("pk") String pk
    );

    @GET("type")
    Call<Poi2Response> listPOI2(
            @Query("pk") String pk
    );

    @GET("users")
    Call<UsersResponse> listUsers();

    @GET("b/{pk}")
    Call<PlaceResponse> getPlaceDetail(
            @Path("pk") String pk
    );

    @GET("message/{pk}")
    Call<MessageResponse> getMessageDetail(
            @Path("pk") String pk
    );

    @GET("type/{pk}")
    Call<TypeResponse> getTypeDetail(
            @Path("pk") String pk
    );

    @GET("users/{pk}")
    Call<UsersResponse> getUserDetail(
            @Path("pk") String pk
    );
    @GET("/maps/api/geocode/json?")
    Call<GeocodeResponse> getGecodeData(
            @Query("address")String address,
            @Query("key")String key
    );

    @GET("/maps/api/geocode/json?")
    Call<GeocodeResponse> getRevGecodeData(
            @Query("latlng")String latlng,
            @Query("key")String key
    );


}

