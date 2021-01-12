package ehu.isad.Utils;

import com.google.gson.Gson;
import ehu.isad.Book;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Sarea {
    public Book readFromUrl(String isbn) throws IOException {
        URL openlibrary = new URL("https://openlibrary.org/api/books?bibkeys=ISBN:"+isbn+"&jscmd=details&format=json");
        URLConnection yc = openlibrary.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream()));
        String inputLine = in.readLine();
        in.close();

        String[] zatiak = inputLine.split("ISBN:"+isbn+"\":");
        inputLine = zatiak[1].substring(0, zatiak[1].length()-1);
        Gson gson = new Gson();
        Book book=gson.fromJson(inputLine, Book.class);
        return book;
    }
}
