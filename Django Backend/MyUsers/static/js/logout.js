let logoutBtn = document.getElementById("logoutBtn")

        logoutBtn.addEventListener('click', function(e){
            e.preventDefault(e);

            let tokenValue = localStorage.getItem("token")
            
            let logoutBody = "Token " + tokenValue

            console.log(logoutBody)

            fetch('http://127.0.0.1:8000/api/logout/', {
                method: 'POST',
                headers: {"Authorization": logoutBody}
                
            }).then(function (response) {
                if (response.status == 200 || response.status == 204) {
                        window.location = "http://127.0.0.1:8000/"   
                        localStorage.removeItem('token')  
                        localStorage.removeItem('id') 
                        localStorage.removeItem('username')
                        localStorage.removeItem('email')
                        localStorage.removeItem('password')                                     
                        return response.json();
                    } else {
                        console.log("Incorrect token");
                    }
            })
        })