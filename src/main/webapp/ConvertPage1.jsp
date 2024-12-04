<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PDF to Word Converter</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f9;
        }

        /* Navigation Menu */
        nav {
            background-color: #333;
            color: white;
            padding: 15px;
            text-align: center;
        }

        nav a {
            color: white;
            padding: 10px 20px;
            margin: 0 10px;
            text-decoration: none;
            font-weight: bold;
        }

        nav a:hover {
            background-color: #575757;
        }

        /* Logout Button (Top Left) */
        .logout-btn {
            margin: 1rem;
            background-color: #FF4C4C;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
        }

        .logout-btn:hover {
            background-color: #e03e3e;
        }

        /* Page Content */
        .container {
            text-align: center;
            padding: 20px;
        }

        .button-group {
            margin: 20px 0;
        }

        .file-list {
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            gap: 15px;
            margin: 20px auto;
        }

        .file-card {
            width: 150px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            position: relative;
            overflow: hidden;
            text-align: center;
        }

        .file-card img {
            width: 100%;
            height: 100px;
            object-fit: cover;
            background-color: #ddd;
        }

        .file-card .remove-btn {
            position: absolute;
            top: 5px;
            right: 5px;
            background-color: #FF4C4C;
            color: #fff;
            border: none;
            border-radius: 50%;
            width: 25px;
            height: 25px;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
        }

        .file-card .file-name {
            font-size: 14px;
            margin: 10px 0;
        }

        .download-all-btn {
            display: inline-block;
            padding: 10px 20px;
            margin-top: 20px;
            color: #fff;
            background-color: #333;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .progress-container {
            margin-top: 20px;
        }

        .progress-card {
            width: 100%;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            padding: 10px;
            margin-bottom: 10px;
        }

        .progress-bar {
            width: 100%;
            height: 10px;
            background-color: #ddd;
            border-radius: 5px;
            overflow: hidden;
        }

        .progress-bar span {
            display: block;
            height: 100%;
            background-color: #4CAF50;
            transition: width 1s;
        }

        .error-message {
            color: red;
            margin-top: 10px;
        }

        /* Hide Pages */
        .hidden {
            display: none;
        }
    </style>
</head>
<body>
    <!-- Logout Button -->
    <nav>
        <a href="javascript:void(0);" onclick="showPage('convert')">Convert</a>
        <a href="javascript:void(0);" onclick="showPage('progress')">Progress</a>
    </nav>
    <button class="logout-btn" onclick="logout()">Logout</button>
    <h1>PDF to Word Converter</h1>
    <p>Select up to 5 PDF files to convert them into Word files:</p>
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
                        return response.text(); // Server có thể trả về phản hồi đơn giản như trạng thái
                    })
                    .then(data => {
                        console.log('Server response:', data);
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
