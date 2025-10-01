package com.piromant.core.dal;

import com.piromant.dto.request.Redirectable;

public interface RedirectingClient {

    Object redirect(Redirectable request, String url);
}
