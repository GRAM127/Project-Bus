
// 66, 150, 081, 082, 083, 086
// const BusId = [5516, 12018, 12072, 12013, 12029, 2800994]

let busData = {}


function getStationData(direction) {
	try {
		var station = (direction == "LEFT"? "196933" : "196934")
		var url = "https://map.naver.com/v5/api/bus/arrival?lang=ko&stationId=" + station +"&caller=pc_map&output=json"
		var jsoup = org.jsoup.Jsoup.connect(url).ignoreContentType(true).get().text()
		var json = JSON.parse(jsoup).message.result.busArrivalList

		return "[" + json.map(e => {
			var locationList = []
			if (e.locationNo1 != null) {
				locationList.push({
					name: String(getBusStop(e.routeId, direction, Number(e.locationNo1))),
					time: String(Number(e.predictTime1) / 60).split(".")[0]
				})
			}
			if (e.locationNo2 != null) {
				locationList.push({
					name: String(getBusStop(e.routeId, direction, Number(e.locationNo2))),
					time: String(Number(e.predictTime2) / 60).split(".")[0]
				})
			}
			return JSON.stringify({
				name: getBusName(e.routeId),
				locationList: locationList
			})
		}) + "]"

	} catch(e) {
	    android.util.Log.e("#LOG", String(e))
	    android.util.Log.e("#LOG", String(e.lineNumber))
		return String("[]")
    }
}


function getBusName(busID) {
	if (busData[busID] == undefined) updateData(busID)
	return busData[busID].name
}


function getBusStop(busID, direction, location) {
	if (busData[busID] == undefined) updateData(busID)
	return String(busData[busID].busStops[Number(busData[busID].busStops.findIndex(v => v.id == "196933")) + (direction == "RIGHT"? location : -location)].name)
}

function updateData(busID) {
	var url = "https://map.naver.com/v5/api/transit/bus/routes/" + busID + "?lang=ko&caller=naver_map&output=json"
	var jsoup = org.jsoup.Jsoup.connect(url).ignoreContentType(true).get().text()

	var name = JSON.parse(jsoup).name
	var busStops = JSON.parse(jsoup).busStops.map(e => {
	    return {
		    id: e.id,
		    name: e.name
        }
	})

	busData[busID] = {
		name: name,
		busStops: busStops
	}
}