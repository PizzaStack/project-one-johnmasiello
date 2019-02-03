(function () {
    function authenticateEmployee(username, password) {
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4) {
                switch (this.status) {
                    case 200:
                    if (this.responseText != 'success')
                        window.location.replace('../index.html');
                    else
                        onValidatedSession();
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

function onValidatedSession() {
    onShowOnly();
}

var reimbursementModel;

function updateReimbursementModel(reimbursementObj) {
    if (reimbursementModel === undefined) {
        reimbursementModel = new Object();
    }
    reimbursementModel[reimbursementObj['id']] = reimbursementObj;
}
function getReimbursementModel(id) {
    if (reimbursementModel === undefined) {
        reimbursementModel = new Object();
    }
    let model = reimbursementModel[id];
    if (model === undefined)
        model = {id: 0};
    return model;
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
                            tablebody.innerHTML += generateReimbursementRecordInnerHtml(request);
                            updateReimbursementModel(request);
                        });
                        // https://pawelgrzybek.com/loop-through-a-collection-of-dom-elements/
                        const buttons = [...tablebody.querySelectorAll('button')].filter($ => $.classList.contains('edit-trigger'));
                        for (let i = 0; i < buttons.length; i++) {
                            buttons[i].addEventListener("click", function (type, event) {
                                if (type == 'click')
                                    event.stopPropagation();
                                populateEditRequest(reimbursement[i]);
                            },true);
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
        
        let endpoint = pending === undefined ? '../employee-reimbursement-request' :
            `../employee-reimbursement-request?pending=${pending}`;
        // xhttp.responseType='json';
        xhttp.open('GET', endpoint, true);
        xhttp.send();
}

function generateReimbursementRecordInnerHtml(request) {
    return `<tr class="rowItemReimbursement">
        ${generateReimbursementColumnsInnerHtml(request)}
        </tr>`;
}

var date = new Date();
function generateReimbursementColumnsInnerHtml(request) {
    return `<td class="rowitemrequestId">${request['id']}</td>
        <td>$${request['expense']}</td>
        <td>
            <div class="text-center">
                <img src="${request['receiptScan'] ? `data:image/bmp;base64,${request['receiptScan']}` :
                'https://via.placeholder.com/100x100.png?text=Receipt%20Image'}" class="rounded fixedThumbs" alt="img"
                id='main-receipt-img'>
            </div>
        </td>
        <td>${request['description']}</td>
        <td>${!request['resolved'] ? 'pending' :
            request['approved'] ? 'approved' :
                'denied'}</td>
        <td>${request['date'] != null ? request['date'] :
            `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`}</td>
        <td>${request['employeeId']}</td>
        <td>${request['managerId'] <= 0 ? '-' : request['managerId']}</td>
        <td><p data-placement="top" data-toggle="tooltip" title="Edit">
        <button class="btn btn-primary btn-xs edit-trigger" data-title="Edit" data-toggle="modal" data-target="#edit">edit</button></p></td>`;
}

function populateEditRequest(request) {
    file = null;
    document.getElementById('requestnumber-edit').innerText = `# ${request['id']}`;
    document.getElementById('requestexpense-edit').value = request['expense'];
    document.getElementById('requestdescription-edit').value = request['description'];
    document.getElementById('requestsubmit-edit').addEventListener('click', uploadAndSubmit, true);

    function uploadAndSubmit(type, event) {
        $('#edit').modal('hide')
        if (type == 'click')
                    event.stopPropagation();
        if (file)
            startUpload(callback);
        else    
            callback(null);
    
        function callback(file_as_64_str) {
            id = String(document.getElementById('requestnumber-edit').innerText).split(' ')[1];
            let model = getReimbursementModel(id);
            description = document.getElementById('requestdescription-edit').value;
            if (description)
                model['description'] = description;
            expense = document.getElementById('requestexpense-edit').value;
            if (expense)
                model['expense'] = expense;
            if (file_as_64_str)
                model['receiptScan'] = file_as_64_str;
            updateReimbursementModel(model);
            submitImageCallback(model, uiCallback);
        }

        function uiCallback(jsonResponse) {
            const id = jsonResponse['id'];
            let tablebody = document.getElementById('myRequestTableBody');
            const rows = [...tablebody.querySelectorAll('tr')].filter($ => $.classList.contains('rowItemReimbursement') &&
                $.children[0].innerHTML == id);
                if (rows.length === 1) {
                    let row = rows[0];
                    row.innerHTML = generateReimbursementColumnsInnerHtml(jsonResponse);
                    const buttons = [...row.querySelectorAll('button')].filter($ => $.classList.contains('edit-trigger'));
                    if (buttons.length === 1) {
                        buttons[0].addEventListener("click", function (type, event) {
                            if (type == 'click')
                                event.stopPropagation();
                            populateEditRequest(jsonResponse);
                        }, true);
                    }
                }
        }
    }
}

(function() {
    document.getElementById('newRequest').addEventListener("click", function() {
        file = null;
        document.getElementById('requestexpense-create').value = '';
        document.getElementById('requestdescription-create').value = '';
    });
    document.getElementById('requestsubmit-create').addEventListener("click", uploadAndSubmit, true);

    function uploadAndSubmit(type, event) {
        $('#create').modal('hide')
        if (type == 'click')
            event.stopPropagation();
        if (file)
            startUpload(callback);
        else    
            callback(null);
    
        function callback(file_as_64_str) {
            id = 0;
            let model = getReimbursementModel(id);
            description = document.getElementById('requestdescription-create').value;
            if (description)
                model['description'] = description;
            expense = document.getElementById('requestexpense-create').value;
            if (expense)
                model['expense'] = expense;
            if (file_as_64_str)
                model['receiptScan'] = file_as_64_str;
            updateReimbursementModel(model);
            submitImageCallback(model, uiCallback);
        }

        function uiCallback(jsonResponse) {
            let tablebody = document.getElementById('myRequestTableBody');
            tablebody.innerHTML += generateReimbursementRecordInnerHtml(jsonResponse);
            const buttons = [...tablebody.querySelectorAll('button')].filter($ => $.classList.contains('edit-trigger'));
            buttons.pop().addEventListener("click", function (type, event) {
                if (type == 'click')
                    event.stopPropagation();
                populateEditRequest(jsonResponse);
            },true);
        }
    }
})();

var file;
(function() {
    'use strict';

    // UPLOAD CLASS DEFINITION
    // ======================
    // https://bootsnipp.com/snippets/featured/bootstrap-drag-and-drop-upload
    // https://placeholder.com/
    // https://getbootstrap.com/docs/4.0/content/images/
    // https://stackoverflow.com/questions/36280818/how-to-convert-file-to-base64-in-javascript
    // http://jsfiddle.net/eliseosoto/JHQnk/
     function setupDropZone(dropZone) {
        if (!dropZone) {       
            return;
        }
    
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
    var dropZone;
    dropZone = document.getElementById('requestreceipt-drop-zone-create');
    setupDropZone(dropZone);
    dropZone = document.getElementById('requestreceipt-drop-zone-edit');
    setupDropZone(dropZone);
})();

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

function submitImageCallback(model, uiCallback) {
    let body = JSON.stringify({id: model['id'], 
        description: model['description'], 
        expense: model['expense'], 
        date: model['date'],
        employeeId: model['employeeId'],
        approved: model['approved'],
        resolved: model['resolved'],
        managerId: model['managerId'],
        receiptName: model['receiptName'],
        receiptScan: model['receiptScan']});

    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {            
        if (this.readyState == 4) {
            switch (this.status) {
                case 200:
                if (this.responseText) {
                    console.log("SUCCESS making receipt request");
                    let responseObj = JSON.parse(this.responseText);
                    updateReimbursementModel(responseObj);
                    uiCallback(responseObj);
                }
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
    xhttp.open('POST', '../employee-reimbursement-request', true);
    xhttp.setRequestHeader("Content-Type", "application/json");
    xhttp.send(body);
    // write a json object from the servlet:
    // https://github.com/FasterXML/jackson-databind
    // or more generally,
    // https://github.com/FasterXML/jackson-docs
}