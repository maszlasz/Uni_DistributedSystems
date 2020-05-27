import requests
import json
from flask import Flask, render_template, g, request
from requests_oauthlib import OAuth1

app = Flask(__name__)

key_OWM = "ac14a0d7ccfdfe7ae057e17f77a48711"
key_news = "6feddfd2153149aeba3f65c1353feb59"
auth_TNP = OAuth1("3bc9b0321da94c4eb86019aa0286ecfc",
                    "ca13fe26e59241788aede7a2237e3109")


@app.route("/", methods=["GET"])
def get_tasks():
    return render_template("index.html")


@app.route("/<path:dummy>", methods=["GET"])
def fallback(dummy):
    return render_template("dummy.html")


@app.route("/data", methods=["POST"])
def process_data():
    city = request.form["city"].upper()
    icon = request.form["icon"]
    currency = request.form["currency"].upper()

    g.city = city
    try:
        weather_data = get_weather_data(city)
        g.temp = str(round(weather_data["temp"]-273, 2)) + " °C"
        g.temp_min = str(round(weather_data["temp_min"]-273, 2)) + " °C"
        g.temp_max = str(round(weather_data["temp_max"]-273, 2)) + " °C"
        g.humidity = str(weather_data["humidity"]) + "%"
        g.pressure = str(weather_data["pressure"]) + " hPa"
    except (KeyError, TypeError, json.decoder.JSONDecodeError):
        g.temp = ""
        g.temp_min = ""
        g.temp_max = ""
        g.humidity = ""
        g.pressure = ""

    try:
        quote = get_random_pr_quote()
        g.quote = quote["en"]
        g.quote_author = quote["author"]
    except json.decoder.JSONDecodeError:
        g.quote = ""
        g.quote_author = ""

    try:
        news = get_news()
        g.news_title = news["title"]
        g.news_author = news["author"]
        g.news_source = news["source"]["name"]
        g.news_description = news["description"]
    except (KeyError, TypeError, json.decoder.JSONDecodeError):
        g.news_title = ""
        g.news_author = ""
        g.news_source = ""
        g.news_description = ""

    try:
        g.icon = get_icon_data(icon)
    except (KeyError, json.decoder.JSONDecodeError):
        g.icon = "0"

    g.currency = currency
    try:
        g.exchange_rate = get_exchange_rate(currency)
    except (KeyError, json.decoder.JSONDecodeError):
        g.exchange_rate = "0"

    return render_template("results.html")


def get_random_pr_quote():
    data = requests.get("https://programming-quotes-api.herokuapp.com/quotes/random") \
        .json()
    return data


def get_weather_data(city):
    data = requests.get("http://api.openweathermap.org/data/2.5/weather?q={}&appid={}" \
        .format(city, key_OWM)).json()["main"]
    return data


def get_news():
    data = requests.get("http://newsapi.org/v2/top-headlines?country=pl&apiKey={}" \
        .format(key_news)).json()["articles"][0]
    return data


def get_exchange_rate(currency):
    data = requests.get("https://api.exchangeratesapi.io/latest?symbols={}" \
        .format(currency)).json()["rates"][currency]
    return data


def get_icon_data(icon):
    data = requests.get("http://api.thenounproject.com/icon/{}".format(icon), auth=auth_TNP) \
        .json()["icon"]["icon_url"]
    return data


if __name__ == '__main__':
    app.run()
