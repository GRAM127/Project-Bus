

//function getWeather() {
//
//    let data = {
//        temperature: 23,
//        weather: 3, // [맑음, 구름 조금, 구름 많음, 흐림, 비, 가끔 비, 눈, 가끔 눈, 소나기, 천둥번개, 안개, 황사]
//        hourly: [
//            { time: 9, temperature: 23, weather: 4 },
//            { time: 10, temperature: 24, weather: 4 },
//            { time: 11, temperature: 24, weather: 4 }
//        ],
//        pm10: 45,
//        pm2 : 95
//    }
//
//    return JSON.stringify(data)
//}

function getWeather() {
    try {
        var data = org.jsoup.Jsoup.connect("https://m.search.naver.com/search.naver?where=m&sm=mtb_drt&query=%ED%8C%8C%EC%A3%BC%EC%8B%9C%20%EC%9A%B4%EC%A0%953%EB%8F%99%EB%82%A0%EC%94%A8").get();
        main= data.select("div.status_wrap");
        temp_now=main.select("strong").get(0).text().replace(main.select("strong").select("span").get(0).text(), "").replace(main.select("strong").select("span").get(1).text(), "");
        weather=main.select("div.weather_main").get(0).text();
        forc=main.select("div.report_card_wrap").select("div.donut_graph");
        md=forc.get(0).select("span").text();
        hmd=forc.get(1).select("span").text();

        let returnData = {
                temperature: Number(temp_now),
                weather: String(weather), // [맑음, 구름 조금, 구름 많음, 흐림, 비, 가끔 비, 눈, 가끔 눈, 소나기, 천둥번개, 안개, 황사]
//                hourly: [
//                    { time: 9, temperature: 23, weather: 4 },
//                    { time: 10, temperature: 24, weather: 4 },
//                    { time: 11, temperature: 24, weather: 4 }
//                ],
                pm10: Number(md),
                pm2 : Number(hmd)
            }

        return JSON.stringify(returnData)

    } catch (e) {
        return null;
    }
};