var alertRedInput = "#8C1010";
var defaultInput = "rgba(10, 180, 180, 1)";

function userNameValidation(usernameInput) {
	var username = document.getElementById("username");
	var issueArr = [];
	if (/[-!@#$%^&*()_+|~=`{}\[\]:";'<>?,.\/]/.test(usernameInput)) {
		issueArr.push("No special characters!");
	}
	if (issueArr.length > 0) {
		username.setCustomValidity(issueArr);
		username.style.borderColor = alertRedInput;
	} else {
		username.setCustomValidity("");
		username.style.borderColor = defaultInput;
	}
}

function passwordValidation(passwordInput) {
	var password = document.getElementById("password");
	var issueArr = [];
	if (!/^.{7,15}$/.test(passwordInput)) {
		issueArr.push("Password must be between 7-15 characters.");
	}
	if (!/\d/.test(passwordInput)) {
		issueArr.push("Must contain at least one number.");
	}
	if (!/[a-z]/.test(passwordInput)) {
		issueArr.push("Must contain a lowercase letter.");
	}
	if (!/[A-Z]/.test(passwordInput)) {
		issueArr.push("Must contain an uppercase letter.");
	}
	if (issueArr.length > 0) {
		password.setCustomValidity(issueArr.join("\n"));
		password.style.borderColor = alertRedInput;
	} else {
		password.setCustomValidity("");
		password.style.borderColor = defaultInput;
	}
}
function myFunction() {
	console.log("Success");
	$.ajax({
		type : 'GET',
		url : 'http://localhost:8080/income-rs/rest/json',
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		success : function(response) {
			document.getElementById('help').innerHTML = response.result;
			document.getElementById('myModal').style.display = "block";
			console.log(response.result);
		},
		error : function(error) {
			console.log(error);
		}
	});
}

function myFunction1() {
	console.log("Success");
	$.ajax({
		type : 'GET',
		url : 'http://localhost:8080/income-rs/rest/admin',
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		success : function(response) {
			document.getElementById('help').innerHTML = response.result;
			document.getElementById('myModal').style.display = "block";
			console.log(response.result);
		},
		error : function(error) {
			console.log(error);
		}
	});
}

function myFunction3() {
	console.log("Success");
	$.ajax({
		type : 'GET',
		url : 'http://localhost:8080/income-rs/rest/dev',
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		success : function(response) {
			document.getElementById('help').innerHTML = response.result;
			document.getElementById('myModal').style.display = "block";
			console.log(response.result);
		},
		error : function(error) {
			console.log(error);
		}
	});
}

window.onclick = function(event) {
	if (event.target == document.getElementById('myModal')) {
		document.getElementById('myModal').style.display = "none";
	}
}