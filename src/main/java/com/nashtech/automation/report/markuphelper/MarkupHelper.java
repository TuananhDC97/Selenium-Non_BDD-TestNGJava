package com.nashtech.automation.report.markuphelper;

import java.util.List;

import com.aventstack.extentreports.markuputils.Markup;
import com.nashtech.automation.api.APIRequest;
import com.nashtech.automation.api.APIResponse;

import okhttp3.Request;
import okhttp3.Response;

public class MarkupHelper extends com.aventstack.extentreports.markuputils.MarkupHelper{
	
	public static Markup createAPIRequestStep(APIRequest request, APIResponse response) {
        return new APIRequestStep(request,response);
    }

}
