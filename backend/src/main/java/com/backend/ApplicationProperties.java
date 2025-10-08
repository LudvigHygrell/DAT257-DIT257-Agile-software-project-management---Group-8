package com.backend;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="benesphere")
public class ApplicationProperties {

    private boolean debug = false;

    private EmailProperties email;

    private FileProperties file = new FileProperties();

    public static class FileProperties {

        private DirectoryProperties publicDirectories = new DirectoryProperties("public");
        private DirectoryProperties privateDirectories = new DirectoryProperties("private");

        public static class DirectoryProperties {

            private String writeDirectory = "writeable";

            private String readDirectory = "readable";

            public DirectoryProperties() {}

            public DirectoryProperties(String root) {
                readDirectory = root + "/readable";
                writeDirectory = root + "/writeable";
            }

            public String getWriteDirectory() {
                return writeDirectory;
            }

            public String getReadDirectory() {
                return readDirectory;
            }

            public void setWriteDirectory(String directory) {
                writeDirectory = directory;
            }

            public void setReadDirectory(String directory) {
                readDirectory = directory;
            }
        }

        public DirectoryProperties getPublicDirectories() {
            return publicDirectories;
        }

        public DirectoryProperties getPrivateDirectories() {
            return privateDirectories;
        }

        public void setPublicDirectories(DirectoryProperties props) {
            publicDirectories = props;
        }

        public void setPrivateDirectories(DirectoryProperties props) {
            privateDirectories = props;
        }
    }

    public static class EmailProperties {

        private String username = "benesphere";
        private boolean verify;

        public boolean isVerified() {
            return verify;
        }

        public void setVerify(boolean verify) {
            this.verify = verify;
        }

        public boolean getVerify() {
            return verify;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public void setEmail(EmailProperties properties) {
        this.email = properties;
    }

    public EmailProperties getEmail() {
        return email;
    }

    public EmailProperties getEmailProperties() {
        return email;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean getDebug() {
        return debug;
    }

    public boolean inDebug() {
        return debug;
    }

    public boolean inRelease() {
        return !debug;
    }

    public FileProperties getFileProperties() {
        return file;
    }

    public FileProperties getFile() {
        return file;
    }

    public void setFile(FileProperties props) {
        file = props;
    }
}
