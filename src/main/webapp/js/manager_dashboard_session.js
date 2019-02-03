(function () {
    function authenticateManager(username, password) {
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
                        const buttons = [...tablebody.querySelectorAll('button')];
                        const doApprove = buttons.filter($ => $.classList.contains('approve-trigger'));
                        for (let i = 0; i < doApprove.length; i++) {
                            doApprove[i].addEventListener("click", function (type, event) {
                                if (type == 'click')
                                    event.stopPropagation();
                                    addApproveRequest(reimbursement[i], true);
                            },true);
                        }
                        const doDeny = buttons.filter($ => $.classList.contains('deny-trigger'));
                        for (let i = 0; i < doDeny.length; i++) {
                            doDeny[i].addEventListener("click", function (type, event) {
                                if (type == 'click')
                                    event.stopPropagation();
                                    addApproveRequest(reimbursement[i], false);
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
        
        let endpoint = pending === undefined ? '../manage-all-reimbursement-request' :
            `../manage-all-reimbursement-request?pending=${pending}`;
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
        <td><button class="btn btn-success btn-xs approve-trigger">O</button></td>
        <td><button class="btn btn-danger btn-xs deny-trigger">X</button></td>`;
}

function addApproveRequest(model, shouldApprove) {
    makeApprovalRequest(model, shouldApprove, uiCallback);

    function uiCallback(model) {
        const id = model['id'];
        let tablebody = document.getElementById('myRequestTableBody');
        const rows = [...tablebody.querySelectorAll('tr')].filter($ => $.classList.contains('rowItemReimbursement') &&
            $.children[0].innerHTML == id);
            if (rows.length === 1) {
                let row = rows[0];
                row.innerHTML = generateReimbursementColumnsInnerHtml(model);
                const buttons = [...row.querySelectorAll('button')];
                const doApprove = buttons.filter($ => $.classList.contains('approve-trigger'));
                if (doApprove.length === 1) {
                    doApprove[0].addEventListener("click", function (type, event) {
                        if (type == 'click')
                            event.stopPropagation();
                            addApproveRequest(reimbursement[i], true);
                    },true);
                }
                const doDeny = buttons.filter($ => $.classList.contains('deny-trigger'));
                if (doDeny.length == 1) {
                    doDeny[0].addEventListener("click", function (type, event) {
                        if (type == 'click')
                            event.stopPropagation();
                            addApproveRequest(reimbursement[i], false);
                    },true);
                }
            }
    }
}

function makeApprovalRequest(model, approved, uiCallback) {

    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {            
        if (this.readyState == 4) {
            switch (this.status) {
                case 200:
                if (this.responseText) {
                    console.log("SUCCESS making approval request");
                    model['resolved'] = true;
                    model['approved'] = approved;
                    model['managerId'] = this.responseText;
                    updateReimbursementModel(model);
                    uiCallback(model);
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
    xhttp.open('POST', `../manage-employee-reimbursement-request?requestId=${model['id']}&approved=${approved}`, true);
    xhttp.setRequestHeader("Content-Type", "application/json");
    xhttp.send();
    // write a json object from the servlet:
    // https://github.com/FasterXML/jackson-databind
    // or more generally,
    // https://github.com/FasterXML/jackson-docs
}