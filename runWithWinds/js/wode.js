//判断图片是否存在
function CheckImgExists(imgurl) {
	var ImgObj = new Image(); //判断图片是否存在  
	ImgObj.src = imgurl;
	//存在图片
	if (ImgObj.fileSize > 0 || (ImgObj.width > 0 && ImgObj.height > 0)) {
		console.log('图片地址有效')
		return true;
	} else {
		console.log('图片地址无效')
		return false;
	}
}
//获取路径参数
function getQueryVariable(variable) {
	var query = window.location.search.substring(1);
	var vars = query.split("&");
	for (var i = 0; i < vars.length; i++) {
		var pair = vars[i].split("=");
		if (pair[0] == variable) {
			return pair[1];
		}
	}
	return (false);
}
//分转时分格式
function ChangeHourMinutestr(str) {
	if (str !== "0" && str !== "" && str !== null) {
		return ((Math.floor(str / 60)).toString().length < 2 ? "0" + (Math.floor(str / 60)).toString() :
			(Math.floor(str / 60)).toString()) + ":" + ((str % 60).toString().length < 2 ? "0" + (str % 60).toString() : (str %
			60).toString());
	} else {
		return "";
	}
}
//秒转时分秒格式
function formatSeconds(time) {
	let min = Math.floor(time % 3600)
	let val = this.formatBit(Math.floor(time / 3600)) + ':' + this.formatBit(Math.floor(min / 60)) + ':' + this.formatBit(
		time % 60)
	return val
}

function formatBit(val) {
	val = +val
	return val > 9 ? val : '0' + val
}


/**判断是否是手机号**/
function isPhoneNumber(tel) {
    var reg =/^0?1[3|4|5|6|7|8][0-9]\d{8}$/;
    return reg.test(tel);
}

//var httpurl = 'http://192.168.0.21:8087/'
var httpurl = 'http://8.140.170.161:8084/'