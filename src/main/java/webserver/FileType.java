package webserver;

public enum FileType {
        HTML("text/html;charset=utf-8"),
        CSS("text/css"),
        SVG("image/svg+xml"),
        PNG("image/png"),
        ICO("image/x-icon"),
        TXT("text/plain"),
        NONE("none");


        private final String contentType;

        FileType(String contentType) {
            this.contentType = contentType;
        }

        public String getContentType() {
            return contentType;
        }
    }
