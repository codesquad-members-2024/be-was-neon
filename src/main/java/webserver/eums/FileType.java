package webserver.eums;

public enum FileType {
        HTML("text/html; charset=utf-8"),
        CSS("text/css"),
        SVG("image/svg+xml"),
        PNG("image/png"),
        ICO("image/x-icon"),
        TXT("text/plain"),
        NONE("none"),
        JS("Application/javascript"),
        URLENCODED("application/x-www-form-urlencoded");


        private final String mimeType;

        FileType(String mimeType) {
            this.mimeType = mimeType;
        }

        public String getMimeType() {
            return mimeType;
        }
    }
