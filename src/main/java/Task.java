import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.time.LocalDate;
        import java.util.Scanner;

public class Task {
    private static final String str_url = "https://openexchangerates.org/api/historical/";
    private static final String apiID = "49ce2a5bb691459e9c574b383bd990bb";
    private static LocalDate date = LocalDate.now();
    private static HttpURLConnection connection;

    public static void main(String[] args) {
        System.out.print("Введите название валюты, курс которой хотите узнать: ");
        Scanner sc = new Scanner(System.in);
        String currency = sc.nextLine();
        printCurrency(getInfo(), currency);
    }

    private static void printCurrency(JSONObject jsonObject, String currency){
        if (jsonObject == null){
            System.err.println("Сервис временно недосутпен :(");
            return;
        }
        JSONObject rates = jsonObject.getJSONObject("rates");
        if (rates.has(currency)){
            System.out.println(String.format("Курс валюты %s относительно USD: %s", currency, rates.get(currency)));
        }
        else System.err.println(String.format("Валюта под названием %s не найдена :(", currency));
    }

    private static JSONObject getInfo(){
        String request = String.format(str_url + "%s.json?app_id=" + apiID, date.toString());
        StringBuilder responseBuilder = new StringBuilder("");

        try {
            URL url = new URL(request);
            connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null){
                responseBuilder.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(responseBuilder.toString());
            return jsonResponse;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) connection.disconnect();
        }
        return null;
    }
}
