function printJson(jsonObject) {
    Object.keys(jsonObject).forEach(($) => console.log(`${$}: ${jsonObject[$]}`));
}

(function () {
    function authenticateEmployee(username, password) {
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4) {
                console.log(this.status);
                switch (this.status) {
                    case 200:
                        if (this.responseText != 'success')
                        window.location.replace('../index.html');
                        break;

                    case 400:
                    console.log("FAIL");
                    break;
                    
                    case 500:
                        // window.location.href='../404.html';
                        break;
                }
            }
        };
        
        xhttp.open('GET', '../validate-employee', true);
        xhttp.send();
    }
    authenticateEmployee();
})();

(function() {
    function logoutEmployee(e) {
        let result = window.confirm('Sure you want to log out?');
        if (result)
            logoutSession();
    }

    function logoutSession() {
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4) {
                console.log(this.status);
                switch (this.status) {
                    case 200:
                        window.location.replace('../index.html');
                        break;
    
                    case 400:
                    console.log("FAIL");
                    break;
                    
                    case 500:
                        // window.location.href='../404.html';
                        break;
                }
            }
        };
        
        xhttp.open('GET', '../login-employee', true);
        xhttp.send();
    }

    let elem = document.getElementById('employeeLogout');
    if (elem)
        elem.addEventListener('click', logoutEmployee, true);
})();

(function() {
    function testMakeReceiptNoImage() {
        let body = JSON.stringify({description: "travel expense", expense: 250, approved: false, resolved: false, receiptScan: null, receiptName: "receipt 1"});

        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {            
            if (this.readyState == 4) {
                switch (this.status) {
                    case 200:
                    console.log(this.responseText);
                    if (this.responseText === 'success')
                        console.log("SUCCESS making receipt request");
                    else
                        console.log("FAIL making receipt request");
                    break;
    
                    case 400:
                        console.log("FAIL making receipt request");
                    break;
                    
                    case 500:
                        // window.location.href='../404.html';
                        break;
                }
            }
        };
        
        xhttp.open('POST', '../submit-reimbursement_request', true);
        xhttp.setRequestHeader("Content-Type", "application/json");
        xhttp.send(body);
        // write a json object from the servlet:
        // https://github.com/FasterXML/jackson-databind
        // or more generally,
        // https://github.com/FasterXML/jackson-docs
    }

    function testFetchReceiptNoImage() {
        let receiptId = 1;

        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {            
            if (this.readyState == 4) {
                switch (this.status) {
                    case 200:
                        reimbursementInfo = this.responseText;
                        printJson(JSON.parse(reimbursementInfo));
                    break;
    
                    case 400:
                        console.log("FAIL receiving receipt response");
                    break;
                    
                    case 500:
                        // window.location.href='../404.html';
                        break;
                }
            }
        };
        
        xhttp.open('GET', `../submit-reimbursement_request?receipt_id=${receiptId}`, true);
        xhttp.send();
    }

    // Test the employee
    let elem = document.getElementById('testEmployeeReceipt');
    if (elem)
        elem.addEventListener('click', testMakeReceiptNoImage, true);
})();

(function() {
    function testMakeEmployeeInfo() {
        let body = JSON.stringify({email: 'jmasgcc@gmail.com', firstname: 'John', lastname: 'Masiello', 
        residentAddress: {streetAddress: '3300 South Cooper St', city: 'Dallas', zipcode: '76022', state: 'TX'}});

        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {            
            if (this.readyState == 4) {
                switch (this.status) {
                    case 200:
                    console.log(this.responseText);
                    if (this.responseText === 'success')
                        console.log("SUCCESS making employee info request");
                    else
                        console.log("FAILmaking employee info request");
                    break;
    
                    case 400:
                        console.log("FAIL making employee info request");
                    break;
                    
                    case 500:
                        // window.location.href='../404.html';
                        break;
                }
            }
        };
        
        xhttp.open('POST', '../employee-info', true);
        xhttp.setRequestHeader("Content-Type", "application/json");
        xhttp.send(body);
        // write a json object from the servlet:
        // https://github.com/FasterXML/jackson-databind
        // or more generally,
        // https://github.com/FasterXML/jackson-docs
    }

    function testFetchEmployeeInfo() {
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {            
            if (this.readyState == 4) {
                switch (this.status) {
                    case 200:
                        reimbursementInfo = this.responseText;
                        printJson(JSON.parse(reimbursementInfo));
                    break;
    
                    case 400:
                        console.log("FAIL receiving employee info");
                    break;
                    
                    case 500:
                        // window.location.href='../404.html';
                        break;
                }
            }
        };
        
        // TODO extract employeeId out of the session
        xhttp.open('GET', "../employee-info", true);
        xhttp.send();
    }

    // Test the employee
    let elem = document.getElementById('testEmployeeInfo');
    if (elem)
        elem.addEventListener('click', testMakeEmployeeInfo, true);
})();