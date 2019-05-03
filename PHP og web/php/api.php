<?php

include_once("config.php");
$possible_url_actions = array("login", "register", "changePassword", "sumreg", "uploadcomplete");

if (isset($_GET["action"]) && in_array($_GET["action"], $possible_url_actions)) {
	switch ($_GET["action"]) {
		case "login": //--------------------------LOGIN-------------------------------
			$user = $_POST["user"];
			$password = $_POST["password"];

			$response["success"] = true;
			$response["error"] = "";

			$statement = mysqli_prepare($conn, "SELECT user_id, password FROM users WHERE user = ? LIMIT 1");
			mysqli_stmt_bind_param($statement, "s", $user);
			mysqli_stmt_execute($statement);
			mysqli_stmt_bind_result($statement, $userID, $hash);

			if(!mysqli_stmt_fetch($statement)){
				$response["error"] = "Bruker finnes ikke";
				$response["success"] = false;
				echo json_encode($response);
				exit;
			}
			if(!password_verify($password, $hash)){
				$response["error"] = "Feil passord";
				$response["success"] = false;
			}
			$response["userID"] = $userID;
			break;
		case "register": //--------------------------REGISTRER-------------------------------
			$user = $_POST["user"];
			$password = $_POST["password"];
			$hash = password_hash($password, PASSWORD_DEFAULT);

			$response = array();
			$selectStmt = mysqli_prepare($conn, "SELECT * FROM users WHERE user = ?");
			mysqli_stmt_bind_param($selectStmt, "s", $user);
			mysqli_stmt_execute($selectStmt);

			$response["error"] = $user;
			$response["success"] = false;
			if (mysqli_stmt_fetch($selectStmt)){
				$response["error"] = "Username already exists";
				echo json_encode($response);
				exit;
			}
			$insertStmt = mysqli_prepare($conn, "INSERT INTO users (user, password) VALUES(?, ?);");
			mysqli_stmt_bind_param($insertStmt, "ss", $user, $hash);

			if(mysqli_stmt_execute($insertStmt)){
				$response["success"] = true;
				$statement = mysqli_prepare($conn, "SELECT user_id FROM users WHERE user = ? LIMIT 1");
				mysqli_stmt_bind_param($statement, "s", $user);
				mysqli_stmt_execute($statement);
				$result = $statement->get_result();
				$row = $result->fetch_assoc();
				$user_id = $row['user_id'];
				$response["user_id"] = $user_id;
			} else {
				$response["success"] = false;
			}

			$response["user_id"] = $user_id;
			break;
		case "changePassword": //--------------------------CHANGE PASSWORD---------------------
			$userId =(int)$_POST["userID"];
			$oldpassword = $_POST["oldPass"];
			$newpassword = $_POST["newPass"];
			$newhash = password_hash($newpassword, PASSWORD_DEFAULT);
			
			$selectStmt = mysqli_prepare($conn, "SELECT password FROM users WHERE user_id = ? LIMIT 1");
			mysqli_stmt_bind_param($selectStmt, "i", $userId);
			mysqli_stmt_execute($selectStmt);
			mysqli_stmt_bind_result($selectStmt, $hash);
			mysqli_stmt_fetch($selectStmt);

			if(!password_verify($oldpassword, $hash)){
				$response["error"] = "Feil passord";
				$response["success"] = false;
				break;
			}
			mysqli_stmt_close($selectStmt);
			$updateStatement = mysqli_prepare($conn, "UPDATE users SET password = ? WHERE user_id = ?;");
			mysqli_stmt_bind_param($updateStatement, "si", $newhash, $userId);
			if(mysqli_stmt_execute($updateStatement)){
				$response["success"] = true;
			} else {
				$response["success"] = false;
				$response["error"] = mysqli_error($conn);
			}
			mysqli_stmt_close($updateStatement);
			break;
		case "sumreg": //----------------------------SUMREG------------------------------------------
			$json = json_decode(file_get_contents('php://input'),true);
			$userid = $json["userID"];
			$response = array();

			$totalstatement = mysqli_prepare($conn, "SELECT COUNT(*) as total FROM registrations as r WHERE r.user_id = ?");
			mysqli_stmt_bind_param($totalstatement, "i", $userid);
			mysqli_stmt_execute($totalstatement);
			$result = $totalstatement->get_result();
			$row = $result->fetch_assoc();
			$total = $row['total'];

			$response["total"] = $total;

					//----------------------------------------------------------------------------------

			$thisYearStatement = mysqli_prepare($conn, "SELECT COUNT(*) as yeartot FROM registrations as r WHERE r.user_id = ? AND YEAR(r.Date) = YEAR(NOW())");
			mysqli_stmt_bind_param($thisYearStatement, "i", $userid);
			mysqli_stmt_execute($thisYearStatement);
			$result = $thisYearStatement->get_result();
			$row = $result->fetch_assoc();
			$yeartot = $row['yeartot'];

			$response["year_total"] = $yeartot;
					//----------------------------------------------------------------------------------

			$monthstatement = mysqli_prepare($conn, "SELECT COUNT(*) as monthtot FROM registrations as r WHERE r.user_id = ? AND DATEDIFF(CURDATE(), r.Date) <= 30");
			mysqli_stmt_bind_param($monthstatement, "i", $userid);
			mysqli_stmt_execute($monthstatement);
			$result = $monthstatement->get_result();
			$row = $result->fetch_assoc();
			$monthtot = $row['monthtot'];

			$response["month_total"] = $monthtot;
					//----------------------------------------------------------------------------------

			$dayStatement = mysqli_prepare($conn, "SELECT COUNT(*) as daytot FROM registrations as r WHERE r.user_id = ? AND DATE(r.Date) = DATE(NOW())");
			mysqli_stmt_bind_param($dayStatement, "i", $userid);
			mysqli_stmt_execute($dayStatement);
			$result = $dayStatement->get_result();
			$row = $result->fetch_assoc();
			$daytot = $row['daytot'];

			$response["day_total"] = $daytot;
			break;
		case "uploadcomplete": 
			$json = json_decode(file_get_contents('php://input'),true);
			$imagepath = $json["name"];
			$image = $json["image"];
			$userID = $json["userID"];
			$latitude = $json["latitude"];
			$longitude = $json["longitude"];
			$type = $json["type"];
			$size = $json["size"];
			$mountain = $json["Mountain"];
			$forest = $json["Forest"];
			$river = $json["River"];
			$coast = $json["Coast"];
			$lake = $json["Lake"];
			$road = $json["Road"];
			$industry_towns = $json["Industry_Towns"];
			$school_rec = $json["School_Recreational_area"];
			$acre_agriculture = $json["Acre_Agriculture"];
			$residential_area = $json["Residential_area"];
			$response = array();
			$webPath = "https://itfag.usn.no/grupper/v19gr2/plast/itfag/uploadedFiles/";
			$fullPath = $webPath . $imagepath;
			$decodedImage = base64_decode("$image");
			$imageDir = "uploadedFiles/";
			$return = file_put_contents("uploadedFiles/".$imagepath.".JPG", $decodedImage);
			if($return !== false){
				$response['message'] .= "Image Uploaded Successfully";
			}else{
				$response['message'] .= "Image Uploaded Failed";
			}
			$statement = mysqli_prepare($conn, "INSERT INTO registrations (user_id, path, type, size, Mountain, Forest, River, Coast, Lake, Road, Industry_Towns, School_Recreational_area, Acre_Agriculture, Residential_area, latitude, longitude)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			mysqli_stmt_bind_param($statement, "isssiiiiiiiiiiss", $userID, $fullPath, $type, 
				$size, $mountain, $forest, $river, $coast, $lake, $road, $industry_towns, $school_rec, $acre_agriculture, 
				$residential_area, $latitude, $longitude);
			if(!mysqli_stmt_execute($statement)){
				$response["error"] = $conn->error;
				$response["message"] = "Registration failed \n";
			} else {
				$response["message"] = "Registration Uploaded Successfully \n";
			}
			break;
		} // End of Switch
	}

	echo json_encode($response);

	$conn->close();
	?>