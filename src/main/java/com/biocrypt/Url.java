package com.biocrypt;

import java.util.List;

public class Url {
    public static final String SHARE_PATH = "./src/main/resources/shares/";
    public static final String GENERATED_SHARE_PATH = "./src/main/resources/generated/";
    public static final String OUTPUT_PATH = "./src/main/resources/output/";
    public static final String CURRENT_OUTPUT_PATH = "./src/main/resources/current-output/";
    public static final String INPUT_PATH = "./src/main/resources/input/";

    public static final String REST_SERVER_IP = "192.168.43.86";
    public static final String PORT = "8080";
    public static final List<String> nodes = List.of(
            "192.168.43.86",
            "192.168.43.216",
            "192.168.43.188"
    );

    public static final String CREATE_SHARE_URL = "/apitest/testing/";
    public static final String GET_COORDINATES_URL = "http://" + REST_SERVER_IP + ":" + PORT + "/registration/getCoordinates/";
    public static final String VERIFY_PIN_URL = "http://" + REST_SERVER_IP + ":" + PORT + "/registration/verifyPin/";
    public static final String RETURN_SHARES_URL = "/apitest/returnShares/";
    public static final String REGISTER_URL = "http://" + REST_SERVER_IP + ":" + PORT + "/registration/register/";
    public static final String UPLOAD_FINGERPRINT_URL = "http://" + REST_SERVER_IP + ":" + PORT + "/registration/uploadFingerprint/";
    public static final String GET_USERS_URL = "http://" + REST_SERVER_IP + ":" + PORT + "/registration/get-users/";


    public static final String DELETE_SHARES_URL = "/apitest/delete-shares/";
    public static final String DELETE_USER_MAPPINGS_URL = "/apitest/delete-user-mappings/";
    public static final String DELETE_USERS_URL = "/apitest/delete-users/";
}
