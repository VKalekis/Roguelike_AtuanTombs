package Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TEst {
    public static void main(String[] args) throws IOException {
        TEst tEst = new TEst();

        System.out.println(tEst.getResourceFiles(""));
    }

    public List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();
        try (
                InputStream in = classLoader.getResourceAsStream(path);
                BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }

        return filenames;
    }

    public InputStream getResourceAsStream(String resource) {
        final InputStream in
                = getContextClassLoader().getResourceAsStream(resource);

        return in == null ? getClass().getResourceAsStream(resource) : in;
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
