
function getTime(){
	return 1.2;
}
function getDuration(strategy, hiddenChallenge, passPicIdx){
	var hiddenMap = new java.util.HashMap();
	// Create a hashmap to store all hidden challeng button layouts
	var layOut = new java.util.ArrayList();
	layOut.add(1);
	layOut.add(2);
	layOut.add(3);
	layOut.add(4);
	layOut.add(5);
	hiddenMap.put("hidden up", layOut);
	var layOut = new java.util.ArrayList();
	layOut.add(5);
	layOut.add(1);
	layOut.add(2);
	layOut.add(3);
	layOut.add(4);
	hiddenMap.put("hidden down", layOut);
	var layOut = new java.util.ArrayList();
	layOut.add(3);
	layOut.add(4);
	layOut.add(5);
	layOut.add(1);
	layOut.add(2);
	hiddenMap.put("hidden left", layOut);
	var layOut = new java.util.ArrayList();
	layOut.add(2);
	layOut.add(3);
	layOut.add(4);
	layOut.add(5);
	layOut.add(1);
	hiddenMap.put("hidden right", layOut);
	var layOut = new java.util.ArrayList();
	layOut.add(4);
	layOut.add(5);
	layOut.add(1);
	layOut.add(2);
	layOut.add(3);	
	hiddenMap.put("hidden centre", layOut);
	// Caculate the hidden response corresponds to the hidden challenge and position of pass picture
	// Strategy 1: (Undercover paper)locate the hidden challenge button panel, find the number(passPicIdx) from the button panel, and return the position of this button
	//              e.g. if hidden challenge is left, the passPicIdx is 3 (4th picture), then the public response is 1.
	if (hiddenChallenge=="hidden up")
	{
		return 0.0;
	}	
	else{
		var count = 1;
		var step = 0;
		if (strategy==1){			
			var layOut = hiddenMap.get(hiddenChallenge);
			for(var i=0;i<layOut.size();i++){
				if(layOut.get(i)!=passPicIdx+1){										
					count += 1
				}else{
					step = count;
					count = 1;
				}
			}
		}	
	// Strategy 2: locate the hidden challenge button panel, find the button at the position of passPicIdx, and return the number on this button
	//             e.g. if the hidden challenge is left, the passPicIdx is 3 (4th picture), then the public response is 3 (4th button)
        if (strategy==2){
			var layOut = hiddenMap.get(hiddenChallenge);
			publicResponseIdx = layOut.get(passPicIdx+1)-1;
		}
		return 0.25*step;//parseInt(publicResponseIdx);
	}	
}

function getHiddenResponse(publicChallenge, passPic){
	/* 
	This function is to return the public challenge repsponse
	If the pass picture is exists, it returns the pass pic position ranged from 1-4
	If the pass picture is absence, it returns the 5
	*/
	var hiddenResponseIdx = 10;
	for (var i=0;i<publicChallenge.size();i++){
		var number = publicChallenge.get(i);
		for (var j=0;j<passPic.size();j++){
			var passPicNum = passPic.get(j);
			if(number == passPicNum){
				hiddenResponseIdx = i;
				break;				
			}
		}
	}
	if(hiddenResponseIdx==10){
		hiddenResponseIdx = 4;
	}	
	return parseInt(hiddenResponseIdx);
}

function getHiddenChallenge() {
    var group;
	var i = Math.floor((Math.random() * 5) + 1);
	switch(i){
		case 1.0:
		    group = "hidden up";
			break;
		case 2.0:
		    group = "hidden down";
			break;
		case 3.0:
		    group = "hidden left";
			break;
		case 4.0:
		    group = "hidden right";
			break;
		case 5.0:
    		group = "hidden centre";
			break;
	}
	return group;
}

function getPublicResponse(strategy, hiddenChallenge, passPicIdx){
	/*
	This function is to get public response
	*/
	// passPicIdx is from 0 to 4
	var hiddenMap = new java.util.HashMap();
	// Create a hashmap to store all hidden challeng button layouts
	var layOut = new java.util.ArrayList();
	layOut.add(1);
	layOut.add(2);
	layOut.add(3);
	layOut.add(4);
	layOut.add(5);
	hiddenMap.put("hidden up", layOut);
	var layOut = new java.util.ArrayList();
	layOut.add(5);
	layOut.add(1);
	layOut.add(2);
	layOut.add(3);
	layOut.add(4);
	hiddenMap.put("hidden down", layOut);
	var layOut = new java.util.ArrayList();
	layOut.add(3);
	layOut.add(4);
	layOut.add(5);
	layOut.add(1);
	layOut.add(2);
	hiddenMap.put("hidden left", layOut);
	var layOut = new java.util.ArrayList();
	layOut.add(2);
	layOut.add(3);
	layOut.add(4);
	layOut.add(5);
	layOut.add(1);
	hiddenMap.put("hidden right", layOut);
	var layOut = new java.util.ArrayList();
	layOut.add(4);
	layOut.add(5);
	layOut.add(1);
	layOut.add(2);
	layOut.add(3);	
	hiddenMap.put("hidden centre", layOut);
	// Caculate the hidden response corresponds to the hidden challenge and position of pass picture
	// Strategy 1: (Undercover paper) locate the hidden challenge button panel, find the number(passPicIdx) from the button panel, and return the position of this button
	//              e.g. if hidden challenge is left, the passPicIdx is 3 (4th picture), then the public response is 1.
	var publicResponseIdx = 0;
	if (strategy==1){
		var layOut = hiddenMap.get(hiddenChallenge);
		for(var i=0;i<layOut.size();i++){
			if(layOut.get(i)==passPicIdx+1){
				publicResponseIdx = i;
			}
		}
	}	
	// Strategy 2: locate the hidden challenge button panel, find the button at the position of passPicIdx, and return the number on this button
	//             e.g. if the hidden challenge is left, the passPicIdx is 3 (4th picture), then the public response is 3 (4th button)
	if (strategy==2){
		var layOut = hiddenMap.get(hiddenChallenge);
		publicResponseIdx = layOut.get(passPicIdx+1)-1;
	}
	return parseInt(publicResponseIdx);
}

function generatePassPicture(max, min, length) {
	/*
	This function is to get random a number of pass pictures from a image pool of pictures 
	*/
	var passPic = new java.util.ArrayList();
	var count = 0;
	while(count<length){
		var temp = Math.floor((Math.random() * max) + min);
		if(!passPic.contains(temp)){
			passPic.add(temp);
			count++;
		} 
	}	
    return passPic;
}

function randomShuffle(array) {
  /*
  This function is to random shuffle the order of pictures/pass pictures
  reference: http://stackoverflow.com/questions/2450954/how-to-randomize-shuffle-a-javascript-array	
  */
  var currentIndex = array.size();
  var temporaryValue;
  var randomIndex;
  // While there remain elements to shuffle...
  while (0 !== currentIndex) {
    // Pick a remaining element...
    randomIndex = Math.floor(Math.random() * currentIndex);
    currentIndex -= 1;

    // And swap it with the current element.
    temporaryValue = array.get(currentIndex);
    array.set(currentIndex,array.get(randomIndex));
	array.set(randomIndex,temporaryValue);
  }
  return array;
}

function arrangeChallenge(passPic, numOfChallenge){
	/*
	This function is to generate 7 challenges given a array of pass pictures. 
	5 challenges contains one unique pass picture, and 2 challenges contains no pass pictures
	randomshuffle is used to randomise the order of pictures for each challenge, and randomise the order for all challenges.
	*/
	var challengeList = new java.util.ArrayList();
	var numberList = new java.util.ArrayList();
	var temp_pass = new java.util.ArrayList();
    
	// Define the indexes for two challenges with no pass picture
	var flag = 0;
	var noPassIdx1 = Math.floor((Math.random() * numOfChallenge)+1);
	var noPassIdx2 = 0;   
	while(flag == 0){
		var temp = Math.floor((Math.random() * numOfChallenge)+1);
		if(temp!=noPassIdx1){
			noPassIdx2 = temp
			flag = 1;
		}
	}		
	// Arrange 5 challenges with pass pictures	
	for(var i=0;i<numOfChallenge;i++){	
		if(i!=noPassIdx1 && i!=noPassIdx2){
			var tempList = new java.util.ArrayList();
			var passFlag = 0;
			var passPicNum = 0;
			// For each challenge, get one pass picture, and make sure it is not duplicated
			var count = 0;
			while(count<1){
				var passIdx = Math.floor((Math.random() * 5) + 1);	
				//print('--');	
				if(temp_pass.size()==5){
					count++;
				}
				if(!temp_pass.contains(passIdx)){					
					passPicNum = passPic.get(passIdx-1);
					temp_pass.add(passIdx);
					//print(passIdx);	
					count++;
				}
			}			           
			// Add other 3 distractor pictures
				var count1 = 0;
				while(count1<3){
					var num = Math.floor((Math.random() * 28) + 1);
					//print(num);
					if(!numberList.contains(num) && !passPic.contains(num)){
						numberList.add(num);				
						tempList.add(num);
						count1++;
					}
				}			
			// Add pass picture
			tempList.add(passPicNum);	
            tempList = randomShuffle(tempList);			
			challengeList.add(tempList);						
		}	
		// Arrange 2 challenges with no pass pictures
		else{
			var tempList = new java.util.ArrayList();
			// Add 4 distractor pictures			
			var count2 = 0;				
		    while(count2<4){
				var num = Math.floor((Math.random() * 28) + 1);
				//print(num);
				if(!numberList.contains(num) && !passPic.contains(num)){
					numberList.add(num);				
					tempList.add(num);
					count2++;
					}
			}			
			tempList = randomShuffle(tempList);		
			challengeList.add(tempList);	
		}
	}	
	challengeList = randomShuffle(challengeList);	
	return challengeList;
}

