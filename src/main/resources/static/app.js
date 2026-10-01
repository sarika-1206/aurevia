const API_BASE_URL = "/api";

function getToken() {
    return localStorage.getItem("aureviaToken");
}

function setToken(token) {
    localStorage.setItem("aureviaToken", token);
}

function removeToken() {
    localStorage.removeItem("aureviaToken");
    localStorage.removeItem("aureviaUser");
}

function getCurrentUser() {
    const user = localStorage.getItem("aureviaUser");

    if (!user) {
        return null;
    }

    try {
        return JSON.parse(user);
    } catch (error) {
        return null;
    }
}

function setCurrentUser(user) {
    localStorage.setItem(
        "aureviaUser",
        JSON.stringify(user)
    );
}

async function apiRequest(endpoint, options = {}) {

    const headers = {
        "Content-Type": "application/json",
        ...(options.headers || {})
    };

    const token = getToken();

    if (token) {
        headers["Authorization"] = `Bearer ${token}`;
    }

    const response = await fetch(
        `${API_BASE_URL}${endpoint}`,
        {
            ...options,
            headers: headers
        }
    );

    if (!response.ok) {

        let errorMessage = "Something went wrong.";

        try {
            const errorData = await response.json();

            if (errorData.message) {
                errorMessage = errorData.message;
            }
        } catch (error) {
            // Ignore JSON parsing errors.
        }

        throw new Error(errorMessage);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

function logout() {
    removeToken();
    window.location.href = "login.html";
}