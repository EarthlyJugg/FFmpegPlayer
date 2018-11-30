package com.lingtao.core.net;

import android.util.Log;

import com.lingtao.core.net.callback.IError;
import com.lingtao.core.net.callback.IFailure;
import com.lingtao.core.net.callback.IRequest;
import com.lingtao.core.net.callback.ISuccess;
import com.lingtao.core.net.callback.RequestCallback;
import com.lingtao.core.net.download.DownloadHandler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestClient {
    private HashMap<String, Object> PARAMS;
    private String URL;
    private IRequest REQUEST;
    private ISuccess SUCCESS;
    private IFailure FAILURE;
    private IError ERROR;

    private final RequestBody BODY;

    //上传下载
    private File FILE;

    private String DOWNLOAD_DIR;
    private String EXTENSION;
    private String FILENAME;


    protected RestClient(HashMap<String, Object> params,
                      String url,
                      IRequest request,
                      ISuccess success,
                      IFailure failure,
                      IError error,
                      RequestBody body,
                      File file, String downloadDir, String extension, String filename) {
        this.PARAMS = params;
        this.URL = url;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;

        this.FILE = file;
        this.DOWNLOAD_DIR = downloadDir;  ///sdcard/XXXX.ext
        this.EXTENSION = extension;
        this.FILENAME = filename;
    }


    public static RestClientBuilder create() {
        return new RestClientBuilder();
    }

    //开始实现真实的网络操作
    private void request(HttpMethod method) {
        RestService service = RestCreator.getRestService();
        Call<String> call = null;
        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        switch (method) {
            case GET:
                call = service.get(URL, PARAMS);
                break;
            case POST:
                call = service.post(URL, PARAMS);
                break;
            case PUT:
                call = service.put(URL, PARAMS);
                break;
            case DELETE:
                call = service.delete(URL, PARAMS);
                break;
            case UPLOAD:
                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, FILE);
                MultipartBody.Part body = MultipartBody.Part.createFormData(
                        "file", FILE.getName(), requestBody
                );
                call = service.upload(URL, body);
                break;
            default:
                break;
        }
        if (call != null) {
            call.enqueue(getRequestCallback());
        }
    }

    private Callback<String> getRequestCallback() {
        return new RequestCallback(REQUEST, SUCCESS, FAILURE, ERROR);
    }

    //各种请求
    public final void get(){
        request(HttpMethod.GET);
    }
    public final void post(){
        request(HttpMethod.POST);
    }
    public final void put(){
        request(HttpMethod.PUT);
    }
    public final void delete(){
        request(HttpMethod.DELETE);
    }
    public final void upload(){
        request(HttpMethod.UPLOAD);
    }
    public final void download(){
        new DownloadHandler(PARAMS,URL,REQUEST,
                SUCCESS,FAILURE,ERROR,DOWNLOAD_DIR,
                EXTENSION,FILENAME)
                .handleDownload();
    }

    public static void test() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://japi.juhe.cn/joke/")
                .client(RestCreator.OKHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RestService service = retrofit.create(RestService.class);
        Map<String, Object> parames = new HashMap<>();
        parames.put("key", "488c65f3230c0280757b50686d1f1cd5");
        parames.put("sort", "asc");
        parames.put("time", "1418816972");
        Call<String> call = service.get("content/list.from", parames);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("nongyulian", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

}
