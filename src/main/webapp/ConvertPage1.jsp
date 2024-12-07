<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PDF to Word Converter</title>
    
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/Resources/css/convert.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <style>

    </style>
</head>
<body>
    <nav class="navbar">
        <div class="nav-links">
            <a href="<%= request.getContextPath() %>/ConvertPage1.jsp">Convert</a>
            <a href="<%= request.getContextPath() %>/FileProcessingListServlet">Progress</a>
        </div>
	    <form action="LogoutServlet" method="POST">
	    	<button class="logout-btn" type="submit">Logout</button>
		</form>

    </nav>
    <div class="text-info">
        <h1>PDF to Word Converter</h1>
        <p>Select up to 5 PDF files to convert them into Word files:</p>
    </div>
    <div class="container">
        <div class="button-group">
            <input type="file" id="file-input" multiple hidden>
            <button class="upload-btn" onclick="document.getElementById('file-input').click()">Upload Files</button>
            <button class="clear-btn" onclick="clearFiles()">Clear Queue</button>
        </div>

        <div class="file-list" id="file-list">
            <!-- File cards will be added dynamically here -->
        </div>
        
        <div class="error-message" id="error-message"></div>

        <button class="download-all-btn" onclick="downloadAll()">⬇️ Download All</button>
    </div>

    <script>
        const fileInput = document.getElementById('file-input');
        const fileList = document.getElementById('file-list');
        const errorMessage = document.getElementById('error-message');
        
        let uploadedFiles = []; //File list
        
        fileInput.addEventListener('change', (event) => {
            const files = Array.from(event.target.files);
            
            if (files.length + fileList.children.length > 5) {
                errorMessage.textContent = 'You can only upload a maximum of 5 files.';
                return;
            }

            errorMessage.textContent = ''; // Clear error message if file count is valid

            files.forEach((file) => {
            	uploadedFiles.push(file); // add File
            	
                const fileCard = document.createElement('div');
                fileCard.className = 'file-card';

                const thumbnail = document.createElement('img');
                thumbnail.src = '<%= request.getContextPath() %>/Resources/images/default-thumbnail.png'; // Replace with actual preview logic if needed
                thumbnail.alt = 'File Thumbnail';

                const removeButton = document.createElement('button');
                removeButton.className = 'remove-btn';
                removeButton.innerHTML = '&times;';
                removeButton.addEventListener('click', () => {
                    fileCard.remove();
                });

                const fileName = document.createElement('div');
                fileName.className = 'file-name';
                fileName.textContent = file.name;
                
                console.log(fileName.textContent);

                const downloadButton = document.createElement('button');
                downloadButton.textContent = 'Download';
                
                downloadButton.addEventListener('click', () => {
                    alert(`Uploading and processing ${file.name}`);
                    
                    const formData = new FormData();
                    formData.append('file', file); // Thêm file vào FormData

                    fetch('<%= request.getContextPath() %>/UploadAndConvertFileServlet', { // URL mapping của servlet
                        method: 'POST',
                        body: formData,
                    })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Failed to process file on server');
                        }
                        return response.text();
                    })
                    .then(data => {
                        console.log('Server response:', data);
                        fileCard.remove();
                        alert('File uploaded and queued for processing.');
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('Failed to upload and process file.');
                    });
                });

                fileCard.appendChild(thumbnail);
                fileCard.appendChild(removeButton);
                fileCard.appendChild(fileName);
                fileCard.appendChild(downloadButton);

                fileList.appendChild(fileCard);
            });
        });

        function clearFiles() {
            fileList.innerHTML = '';
            fileInput.value = ''; // Reset file input
            errorMessage.textContent = ''; // Clear error message
        }

        function downloadAll() {
            alert('Downloading all files...');
        }
    </script>
</body>
</html>
