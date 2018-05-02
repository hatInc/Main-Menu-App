package com.example.harry.mainmenu;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static android.app.Activity.RESULT_OK;

public class connection extends AsyncTask<String,Void,String> {
    private
    Context context;
    String type = "";
    String userName = "";

    connection (Context ctx) {
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        type = params[0];
        String login_url = "http://computing.derby.ac.uk/~hatinc/Team_Project/login.php";
        String register_url = "http://computing.derby.ac.uk/~hatinc/Team_Project/register.php";
        String add_url = "http://computing.derby.ac.uk/~hatinc/Team_Project/add.php";
        String search_random_url = "http://computing.derby.ac.uk/~hatinc/Team_Project/search_random.php";
        String search_value_url = "http://computing.derby.ac.uk/~hatinc/Team_Project/search_value.php";
        String add_basket_url = "http://computing.derby.ac.uk/~hatinc/Team_Project/add_basket.php";
        String search_basket_url = "http://computing.derby.ac.uk/~hatinc/Team_Project/read_basket.php";
        String add_fav_url = "http://computing.derby.ac.uk/~hatinc/Team_Project/add_fav.php";
        String search_fav_url = "http://computing.derby.ac.uk/~hatinc/Team_Project/read_fav.php";
        String delete_basket_url = "http://computing.derby.ac.uk/~hatinc/Team_Project/delete_basket.php";
        String settings_url = "http://computing.derby.ac.uk/~hatinc/Team_Project/settings_add.php";
        String own_url = "http://computing.derby.ac.uk/~hatinc/Team_Project/read_own.php";
        String delete_recipe_url = "http://computing.derby.ac.uk/~hatinc/Team_Project/delete_recipe.php";
        String update_recipe_url = "http://computing.derby.ac.uk/~hatinc/Team_Project/update.php";
        URL url = null;

        String account_username = "";
        String account_email = "";
        String account_password = "";

        String recipe_name = "";
        String recipe_tasks = "";
        String recipe_ingredients = "";

        String search_value = "";
        String user_name = "";
        String data = "";
        String id = "";

        try {

            if(type.equals("login")) {
                url = new URL(login_url);
                account_username = params[1];
                account_password = params[2];
                userName = params[1];
            }
            else if(type.equals("register")){
                url = new URL(register_url);
                account_username = params[1];
                account_email = params[2];
                account_password = params[3];
                userName = params[1];
            }
            else if(type.equals("add")){
                url = new URL(add_url);
                user_name = params[1];
                recipe_name = params[2];
                recipe_tasks = params[3];
                recipe_ingredients = params[4];
            }
            else if(type.equals("search")){
                url = new URL(update_recipe_url);
                id = params[2];
                recipe_name = params[3];
                recipe_tasks = params[4];
                recipe_ingredients = params[5];
            }
            else if(type.equals("search_random")){
                url = new URL(search_random_url);
            }
            else if(type.equals("search_value")){
                url = new URL(search_value_url);
                search_value = params[1];
            }
            else if(type.equals("add_Basket")){
                url = new URL(add_basket_url);
                user_name = params[1];
                recipe_ingredients = params[2];
            }
            else if(type.equals("search_basket")){
                url = new URL(search_basket_url);
                user_name = params[1];
            }
            else if(type.equals("add_fav")){
                url = new URL(add_fav_url);
                user_name = params[1];
                recipe_name = params[2];
            }
            else if(type.equals("search_fav")){
                url = new URL(search_fav_url);
                user_name = params[1];
            }
            else if(type.equals("delete_basket")){
                url = new URL(delete_basket_url);
                user_name = params[1];
                recipe_ingredients = params[2];
            }
            else if(type.equals("apply")){
                url = new URL(settings_url);
                user_name = params[1];
                data = params[2];
            }
            else if(type.equals("own")){
                url = new URL(own_url);
                user_name = params[1];
            }
            else if(type.equals("delete_recipe")){
                url = new URL(delete_recipe_url);
                id = params[1];
            }

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = "";
            if(type.equals("login")) {
                post_data = URLEncoder.encode("account_username", "UTF-8") + "=" + URLEncoder.encode(account_username, "UTF-8") + "&"
                        + URLEncoder.encode("account_password", "UTF-8") + "=" + URLEncoder.encode(account_password, "UTF-8");
            }
            else if(type.equals("register")){
                post_data = URLEncoder.encode("account_username", "UTF-8") + "=" + URLEncoder.encode(account_username, "UTF-8") + "&"
                        + URLEncoder.encode("account_email", "UTF-8") + "=" + URLEncoder.encode(account_email, "UTF-8") + "&"
                        + URLEncoder.encode("account_password", "UTF-8") + "=" + URLEncoder.encode(account_password, "UTF-8");
            }
            else if(type.equals("add")){
                post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                        + URLEncoder.encode("recipe_name", "UTF-8") + "=" + URLEncoder.encode(recipe_name, "UTF-8") + "&"
                        + URLEncoder.encode("recipe_tasks", "UTF-8") + "=" + URLEncoder.encode(recipe_tasks, "UTF-8") + "&"
                        + URLEncoder.encode("recipe_ingredients", "UTF-8") + "=" + URLEncoder.encode(recipe_ingredients, "UTF-8");
            }
            else if(type.equals("search_value")){
                post_data = URLEncoder.encode("search_value", "UTF-8") + "=" + URLEncoder.encode(search_value, "UTF-8");
            }
            else if(type.equals("add_Basket")){
                post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                        + URLEncoder.encode("recipe_ingredients", "UTF-8") + "=" + URLEncoder.encode(recipe_ingredients, "UTF-8");
            }
            else if(type.equals("search_basket")) {
                post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8");
            }
            else if(type.equals("add_fav")){
                post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                        + URLEncoder.encode("recipe_name", "UTF-8") + "=" + URLEncoder.encode(recipe_name, "UTF-8");
            }
            else if(type.equals("search_fav")) {
                post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8");
            }
            else if(type.equals("delete_basket")){
                post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                        + URLEncoder.encode("recipe_ingredients", "UTF-8") + "=" + URLEncoder.encode(recipe_ingredients, "UTF-8");
            }
            else if(type.equals("apply")){
                post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                        + URLEncoder.encode("data", "UTF-8") + "=" + URLEncoder.encode(data, "UTF-8");
            }
            else if(type.equals("own")) {
                post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8");
            }
            else if(type.equals("delete_recipe")) {
                post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            }
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String result="";
            String line="";
            while((line = bufferedReader.readLine())!= null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {

        if (result.equals("register success") || result.equals("login success")) {
            Intent intent = new Intent();
            intent.setClass(context, menu.class);
            intent.putExtra("userName", userName);
            context.startActivity(intent);
        }
        else if (result.equals("register failed")) {
            CharSequence text = "Invalid: Account email already taken";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if (result.equals("login failed")) {
            CharSequence text = "Invalid: Login details incorrect";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if (result.equals("recipe success")) {
            ((maker)context).finish();
        }
        else if (result.equals("recipe failed")) {
            CharSequence text = "Invalid: Recipe";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if (type.equals("search_random")) {
            ((menu)context).setList(result);
        }
        else if (type.equals("own")) {
            ((menu)context).setOwnList(result);
        }
        else if (type.equals("search_value")) {
            ((search)context).setList(result);
        }
        else if (result.equals("basket success")) {
            CharSequence text = "Ingredients added to basket";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if (result.equals("basket failed")) {
            CharSequence text = "Invalid: Items already in basket";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if (type.equals("search_basket")) {
            ((basket) context).setValue(result);
        }
        else if (result.equals("fav success")) {
            CharSequence text = "Recipe added to favourites";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if (result.equals("deleted fav")) {
            CharSequence text = "Recipe removed from favourites";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if (type.equals("search_fav")) {
            ((favourites) context).setList(result);
        }
        else if (result.equals("deleted basket")) {
            CharSequence text = "Deletion Complete";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if (result.equals("basket failed")) {
            CharSequence text = "Invalid: Deletion error";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if (result.equals("dietary success")) {
            CharSequence text = "Dietary Set";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if (result.equals("recipe deleted")){
            ((maker)context).finish();
        }
        else if (result.equals("delete failed")){
            CharSequence text = "Delete failed";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        else if (result.equals("recipe updated")){
            CharSequence text = "Recipe updated";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            ((maker)context).finish();
        }
        else if (result.equals("updated failed")){
            CharSequence text = "Update failed";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}