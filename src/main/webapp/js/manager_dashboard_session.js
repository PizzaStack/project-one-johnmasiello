(function () {
    function authenticateManager(username, password) {
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4) {
                switch (this.status) {
                    case 200:
                        if (this.responseText != 'success')
                        window.location.replace('../index.html');
                        else {
                            onValidatedSession();
                        }
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
        
        xhttp.open('GET', '../validate-manager', true);
        xhttp.send();
    }
    authenticateManager();
})();

(function() {
    function logoutManager(e) {
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
        
        xhttp.open('GET', '../login-manager', true);
        xhttp.send();
    }

    let elem = document.getElementById('managerLogout');
    if (elem)
        elem.addEventListener('click', logoutManager, true);
})();

function onValidatedSession() {
    let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {            
            if (this.readyState == 4) {
                switch (this.status) {
                    case 200:
                    let tablebody = document.getElementById('myRequestTableBody');
                    tablebody.innerHTML = '';
                        let reimbursementInfo = this.responseText;
                        let reimbursement = JSON.parse(reimbursementInfo);
                        // https://stackoverflow.com/questions/19369334/is-there-a-way-to-show-bitmap-data-in-html-image-tag
                        for (const key in reimbursement) {
                            if (reimbursement.hasOwnProperty(key)) {
                                const request = reimbursement[key];

                                tablebody.innerHTML += (`<tr>
                                    <td>${request['id']}</td>
                                    <td>${request['expense']}</td>
                                    <td>
                                        <div class="text-center">
                                            <img src="${request['receiptScan'] ? `data:image/bmp;base64,${request['receiptScan']}` :
                                            'https://via.placeholder.com/100x100.png?text=Receipt%20Image'}" class="rounded fixedThumbs" alt="img"
                                            id='main-receipt-img'>
                                        </div>
                                    </td>
                                    <td>${request['description']}</td>
                                    <td>${request['approved']}</td>
                                    <td>${request['resolved'] ? false : true}</td>
                                    <td>${request['employeeId']}</td>
                                    <td>${request['managerId']}</td>
                                    <td><p data-placement="top" data-toggle="tooltip" title="Edit"><button class="btn btn-primary btn-xs" 
                                    data-title="Edit" data-toggle="modal" data-target="#edit" onclick="populateData(${request});"><span class="glyphicon glyphicon-pencil">edit</span></button></p></td>
                            </tr>`)
                            }
                        }         
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
        
        xhttp.open('GET', '../manage-all-reimbursement-request', true);
        xhttp.send();
}

function onShowOnly(pending) {
    let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {            
            if (this.readyState == 4) {
                switch (this.status) {
                    case 200:
                    let tablebody = document.getElementById('myRequestTableBody');
                    tablebody.innerHTML = '';
                    let reimbursementInfo = this.responseText;
                    let reimbursement = JSON.parse(reimbursementInfo);
                        // https://stackoverflow.com/questions/19369334/is-there-a-way-to-show-bitmap-data-in-html-image-tag
                        reimbursement.forEach(request => {
                            tablebody.innerHTML += (`<tr>
                                <td>${request['id']}</td>
                                <td>${request['expense']}</td>
                                <td>
                                    <div class="text-center">
                                        <img src="${request['receiptScan'] ? `data:image/bmp;base64,${request['receiptScan']}` :
                                        'https://via.placeholder.com/100x100.png?text=Receipt%20Image'}" class="rounded fixedThumbs" alt="img"
                                        id='main-receipt-img'>
                                    </div>
                                </td>
                                <td>${request['description']}</td>
                                <td>${request['approved']}</td>
                                <td>${request['resolved'] ? false : true}</td>
                                <td>${request['employeeId']}</td>
                                <td>${request['managerId']}</td>
                                <td><p data-placement="top" data-toggle="tooltip" title="Edit"><button class="btn btn-primary btn-xs" 
                                data-title="Edit" data-toggle="modal" data-target="#edit" onclick="populateData(${request});"><span class="glyphicon glyphicon-pencil">edit</span></button></p></td>
                           </tr>`)
                        });               
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
        
        xhttp.open('GET', `../manage-all-reimbursement-request?pending=${pending}`, true);
        xhttp.send();
}

function populateData(request) {
    // let request = reimbursement[0];
    console.log('Hello World');
    document.getElementById('requestnumber').innerHTML = request['id'];
    document.getElementById('requestexpense').innerHTML = request['expense'];
    document.getElementById('requestdescription').innerHTML = request['description'];
    // let submitButton = document.getElementById('requestsubmit');
    // submitButton.addEventListener('onclick', startUpload(function(file_as_64_str) {


    //     submitButton.removeEventListener('onclick', this);
    // }));
}

var file;
setEditRequestDropZone();

function setEditRequestDropZone() {
    'use strict';

    // UPLOAD CLASS DEFINITION
    // ======================
    // https://bootsnipp.com/snippets/featured/bootstrap-drag-and-drop-upload
    // https://placeholder.com/
    // https://getbootstrap.com/docs/4.0/content/images/
    // https://stackoverflow.com/questions/36280818/how-to-convert-file-to-base64-in-javascript
    // http://jsfiddle.net/eliseosoto/JHQnk/


    var dropZone = document.getElementById('requestreceipt-drop-zone');
    if (!dropZone)
        return;

    dropZone.ondrop = function(e) {
        e.preventDefault();
        this.className = 'upload-drop-zone';

        file = e.dataTransfer.files[0];
    }

    dropZone.ondragover = function() {
        this.className = 'upload-drop-zone drop';
        return false;
    }

    dropZone.ondragleave = function() {
        this.className = 'upload-drop-zone';
        return false;
    }
}

function startUpload(callback) {
    console.log(file.name);
    console.log(file.type);

    // http://jsfiddle.net/eliseosoto/JHQnk/
    var reader = new FileReader();

    reader.onload = function(readerEvt) {
        var binaryString = readerEvt.target.result;
        callback(btoa(binaryString));
    };
    reader.readAsBinaryString(file);
}