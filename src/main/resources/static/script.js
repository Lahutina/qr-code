function readQRCode() {
    let fileInput = document.getElementById('qr-input');
    let file = fileInput.files[0];
    let reader = new FileReader();

    reader.onload = function (e) {
        let imageData = e.target.result;

        $.ajax({
            url: '/processQRCode',
            type: 'POST',
            data: imageData,
            processData: false,
            contentType: false,
            success: function (response) {
                document.getElementById('qr-text').innerText = "Text in QR code: \n" + response;
                document.getElementById('qr-text').style.margin = "20px";
            },
            error: function () {
                alert("Error reading QR code.");
            }
        });
    };
    reader.readAsDataURL(file);
}

function generateQRCode() {
    let input = $("#input").val();
    let qrCodeImage = $("#qr-image"); // Store the selected element in a variable

    if (input.trim() !== "") {
        $.ajax({
            url: "/generateQRCode",
            type: "POST",
            data: {input: input},
            contentType: "application/x-www-form-urlencoded",
            success: function (data) {
                qrCodeImage.attr("src", "data:image/png;base64," + data);
                qrCodeImage.css("display", "block");
                $("#download-btn").css("display", "block");
                $("#email-btn").css("display", "block");
            },
            error: function () {
                alert("Error generating QR code.");
            }
        });
    } else {
        alert("Please enter input text.");
    }
}


function sendEmailWithPhoto() {
    let qrImageSrc = document.getElementById("qr-image").src;
    let recipientEmail = prompt("Enter recipient email:");

    if (qrImageSrc && recipientEmail) {
        $.ajax({
            url: "/sendEmailWithPhoto",
            type: "POST",
            data: JSON.stringify({ email: recipientEmail, image: qrImageSrc }),
            contentType: "application/json",
            success: function () {
                alert("QR code shared via email successfully");
            },
            error: function (error) {
                alert("Error occurred while sharing QR code via email");
                console.error("Error sharing QR code via email: " + error);
            }
        });
    }
}

function downloadImage() {
    let qrImageSrc = document.getElementById("qr-image").src;
    let link = document.createElement("a");
    link.href = qrImageSrc;
    link.download = "qr-code.png";
    link.click();
}
