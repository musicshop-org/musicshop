package communication.rest;

import javax.ws.rs.core.Response;

public class ResponseWrapper {

    public static ResponseWrapperBuilder builder() {
        return new ResponseWrapperBuilder();
    }

    interface IConfiguration {
        ResponseConfiguration configuration();
    }

    interface IPermission {
        Boolean permission();
    }

    public static class ResponseWrapperBuilder {
        private Boolean considerJWT;
        private Boolean considerPermission;
        private String JWT;
        private Boolean permission;


        private ResponseConfiguration responseConfiguration;

        private Response.Status status;
        private String type;

        private Object entity;

        ResponseWrapperBuilder() {
        }

        public ResponseWrapperBuilder considerJWT(String JWT) {
            this.considerJWT = true;
            this.JWT = JWT;
            return this;
        }

        public ResponseWrapperBuilder considerRoles(Boolean permission) {
            this.considerPermission = true;
            this.permission = permission;
            return this;
        }

        public ResponseWrapperBuilder considerRoles(IPermission myMethodsInterface) {
            this.considerPermission = true;
            this.permission = myMethodsInterface.permission();
            return this;
        }

        public ResponseWrapperBuilder responseConfiguration(ResponseConfiguration responseConfiguration) {
            this.responseConfiguration = responseConfiguration;
            return this;
        }

        public ResponseWrapperBuilder responseConfiguration(IConfiguration configurationInterface) {
            this.responseConfiguration = configurationInterface.configuration();
            return this;
        }

        public Response build() {

            boolean isValidToken = false;

            if (this.considerJWT) {

                if (this.JWT.equals("")) {
                    this.status = Response.Status.UNAUTHORIZED;
                    this.entity = "No authorization provided";
                } else if (!JwtManager.isValidToken(this.JWT)) {
                    this.status = Response.Status.UNAUTHORIZED;
                    this.entity = "Invalid JWT token provided";
                } else {
                    isValidToken = true;
                }
            }

            if (this.considerPermission && !isValidToken) {

                if (this.permission) {
                    this.status = Response.Status.OK;
                } else {
                    this.status = Response.Status.FORBIDDEN;
                    this.entity = "No permission";
                }
            }

            return Response
                    .status(this.status)
                    .entity(this.entity)
                    .type(this.type)
                    .build();
        }
    }
}