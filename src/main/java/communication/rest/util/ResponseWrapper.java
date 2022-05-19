package communication.rest.util;

import communication.rest.JwtManager;
import org.apache.commons.lang3.NotImplementedException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ResponseWrapper {

    public static ResponseWrapperBuilder builder() {
        return new ResponseWrapperBuilder();
    }

    public interface IResponse {
        Response response();
    }

    public static class ResponseWrapperBuilder {
        private Boolean checkCredentials;
        private Boolean loginOk;
        private Boolean considerJWT;
        private Boolean considerPermission;
        private String JWT;
        private Boolean permission;
        private IResponse iResponse;

        ResponseWrapperBuilder() {
        }

        public ResponseWrapperBuilder checkCredentials(Boolean loginOk) {
            this.checkCredentials = true;
            this.loginOk = loginOk;
            return this;
        }

        public ResponseWrapperBuilder checkJWT(String JWT) {
            this.considerJWT = true;
            this.JWT = JWT;
            return this;
        }

        public ResponseWrapperBuilder checkRoles(Boolean permission) {
            this.considerPermission = true;
            this.permission = permission;
            return this;
        }

        public ResponseWrapperBuilder response(IResponse iResponse) {
            this.iResponse = iResponse;
            return this;
        }

        public Response build() {

            if (this.iResponse == null) {
                throw new NotImplementedException("Response must be set");
            }

            boolean validationError = false;

            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            Object entity = "Internal server error, please contact our support";
            String type = MediaType.TEXT_PLAIN;

            if (this.checkCredentials) {

                if (!this.loginOk) {

                    status = Response.Status.UNAUTHORIZED;
                    entity = "Username or password wrong";
                    validationError = true;

                } else if (this.considerPermission) {

                    if (!this.permission) {

                        status = Response.Status.FORBIDDEN;
                        entity = "No permission";
                        validationError = true;

                    }
                }
            } else {

                if (this.considerJWT) {

                    if (this.JWT == null || this.JWT.equals("")) {

                        status = Response.Status.UNAUTHORIZED;
                        entity = "No authorization provided";
                        validationError = true;

                    } else if (!JwtManager.isValidToken(this.JWT)) {

                        status = Response.Status.UNAUTHORIZED;
                        entity = "Invalid JWT token provided";
                        validationError = true;

                    } else if (this.considerPermission) {

                        if (!this.permission) {
                            status = Response.Status.FORBIDDEN;
                            entity = "No permission";
                            validationError = true;
                        }

                    }
                }
            }

            if (validationError) {
                return Response
                        .status(status)
                        .entity(entity)
                        .type(type)
                        .build();
            }

            return this.iResponse.response();
        }
    }
}