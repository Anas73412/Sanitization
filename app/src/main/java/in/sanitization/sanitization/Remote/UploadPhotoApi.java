package in.sanitization.sanitization.Remote;

import in.sanitization.sanitization.Model.ResponseModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 06,August,2020
 */
public interface UploadPhotoApi {
@POST("index.php/api/upload_photo")
    @Multipart
    Call<ResponseModel> uploadPhoto(@Part("user_id")RequestBody user_id,
                                    @Part("name")RequestBody name,
                                    @Part("mobile")RequestBody mobile,
                                    @Part("amount")RequestBody amount,
                                    @Part("txn_id")RequestBody txn_id,
                                    @Part("img_url")RequestBody img_url,
                                    @Part MultipartBody.Part file
                                  );

}
