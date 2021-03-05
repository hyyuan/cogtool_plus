// Strategy coding:
// LR: search from left to right
// RL: search from right to left
// CLR: search starts from the middle, then move left and right
// CRL: search starts from the middle, then move right and left
function getScanPath(strategy, targetIdx, widgetList) {
	var output = new java.util.ArrayList()
		switch (strategy) {
		case "LR":
			for (var i = 0; i < widgetList.size(); i++) {
				if (i != targetIdx) {
					output.add(parseInt(i));
				}
				if (i == targetIdx) {
					output.add(parseInt(i));
					break;
				}
			}
			break;
		case "LR-Scan":
			for (var i = 0; i < widgetList.size(); i++) {
				if (i != targetIdx) {
					output.add(parseInt(i));
				}
				if (i == targetIdx) {
					output.add(parseInt(i));
					break;
				}
			}
			break;
		case "RL":
			for (var i = widgetList.size() - 1; i > -1; i--) {
				if (i != targetIdx) {
					output.add(parseInt(i));
				}
				if (i == targetIdx) {
					output.add(parseInt(i));
					break;
				}
			}
			break;
		case "CLR":
			var flag = true;
			var startingIdx = Math.floor(widgetList.size() / 2);
			for (var i = startingIdx; i > -1; i--) {
				if (i != targetIdx) {
					output.add(parseInt(i));
				}
				if (i == targetIdx) {
					output.add(parseInt(i));
					flag = false;
					break;
				}
			}
			if (flag == true) {
				for (var i = startingIdx; i < widgetList.size(); i++) {
					if (i != targetIdx) {
						output.add(parseInt(i));
					}
					if (i == targetIdx) {
						output.add(parseInt(i));
						flag = false;
						break;
					}
				}
			}
			break;
		case "CRL":
			var flag = true;
			var startingIdx = Math.floor(widgetList.size() / 2);
			for (var i = startingIdx; i < widgetList.size(); i++) {
				if (i != targetIdx) {
					output.add(parseInt(i));
				}
				if (i == targetIdx) {
					output.add(parseInt(i));
					flag = false;
					break;
				}
			}
			if (flag == true) {
				for (var i = startingIdx; i > -1; i--) {
					if (i != targetIdx) {
						output.add(parseInt(i));
					}
					if (i == targetIdx) {
						output.add(parseInt(i));
						flag = false;
						break;
					}
				}
			}
			break;			
		}
		return output;
}