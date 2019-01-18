function printJson(jsonObject) {
    Object.keys(jsonObject).forEach(($) => console.log(`${$}: ${jsonObject[$]}`));
}

function findMagicBytes(jsonObject, magicId) {
    let receiptScan = '';
    jsonObject.forEach(($) => {
        if ($['id'] == magicId) {
            receiptScan = $['receiptScan'];
            return;
        }
    });
    return receiptScan;
}

function findMagicRequest(jsonObject, magicId) {
    let request;
    jsonObject.forEach(($) => {
        if ($['id'] == magicId) {
            request = $;
            return;
        }
    });
    return request;
}

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
        
        xhttp.open('POST', '../employee-reimbursement-request', true);
        xhttp.setRequestHeader("Content-Type", "application/json");
        xhttp.send(body);
        // write a json object from the servlet:
        // https://github.com/FasterXML/jackson-databind
        // or more generally,
        // https://github.com/FasterXML/jackson-docs
    }

    function testFetchReceiptNoImage() {
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {            
            if (this.readyState == 4) {
                switch (this.status) {
                    case 200:
                        reimbursementInfo = this.responseText;
                        reimbursement = JSON.parse(reimbursementInfo);
                        var magicId = 8;                        
                        console.log(findMagicBytes(reimbursement, magicId));
                        // printJson(JSON.parse(reimbursementInfo));
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
        
        xhttp.open('GET', '../employee-reimbursement-request', true);
        xhttp.send();
    }

    function testFetchReceiptWithImage() {
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {            
            if (this.readyState == 4) {
                switch (this.status) {
                    case 200:
                        reimbursementInfo = this.responseText;
                        reimbursement = JSON.parse(reimbursementInfo);
                        var magicId = 10;                 
                        let request = findMagicRequest(reimbursement, magicId);
                        console.log(`id: ${request['id']}, description: ${request['description']}, expense: ${request['expense']}, receipt name: ${request['receiptName']}`);
                        // https://stackoverflow.com/questions/19369334/is-there-a-way-to-show-bitmap-data-in-html-image-tag
                        let elem = document.getElementById('main-receipt-img');
                        elem.setAttribute('src', `data:image/bmp;base64,${request['receiptScan']}`);
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
        
        xhttp.open('GET', '../employee-reimbursement-request', true);
        xhttp.send();
    }

    // Test the employee
    let elem = document.getElementById('testEmployeeReceipt');
    if (elem)
        elem.addEventListener('click', testFetchReceiptWithImage, true);
})();

(function () {
    'use strict';

    // UPLOAD CLASS DEFINITION
    // ======================
    // https://bootsnipp.com/snippets/featured/bootstrap-drag-and-drop-upload
    // https://placeholder.com/
    // https://getbootstrap.com/docs/4.0/content/images/
    // https://stackoverflow.com/questions/36280818/how-to-convert-file-to-base64-in-javascript
    // http://jsfiddle.net/eliseosoto/JHQnk/


    var dropZone = document.getElementById('receipt-drop-zone');
    if (!dropZone)
        return;
    console.log('Drop zone ready!');
    

    var startUpload = function(files) {
        console.log(files);
        let file = files[0];
        console.log(file.name);
        console.log(file.type);

        // http://jsfiddle.net/eliseosoto/JHQnk/
        var reader = new FileReader();

        reader.onload = function(readerEvt) {
            var binaryString = readerEvt.target.result;
            postImageWithReceiptCallback(btoa(binaryString));
        };
        reader.readAsBinaryString(file);
    }

    dropZone.ondrop = function(e) {
        e.preventDefault();
        this.className = 'upload-drop-zone';

        startUpload(e.dataTransfer.files)
    }

    dropZone.ondragover = function() {
        this.className = 'upload-drop-zone drop';
        return false;
    }

    dropZone.ondragleave = function() {
        this.className = 'upload-drop-zone';
        return false;
    }

    function postImageWithReceiptCallback(receipt_scan_64_enc_str) {
        let body = JSON.stringify({description: "travel expense", expense: 250, approved: false, 
        resolved: false, receiptName: "receipt 1", receiptScan: receipt_scan_64_enc_str});

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
        
        xhttp.open('POST', '../employee-reimbursement-request', true);
        xhttp.setRequestHeader("Content-Type", "application/json");
        xhttp.send(body);
        // write a json object from the servlet:
        // https://github.com/FasterXML/jackson-databind
        // or more generally,
        // https://github.com/FasterXML/jackson-docs
    }
})();