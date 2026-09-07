// Use relative path since frontend is served directly by the backend host
const API_URL = "/api/bookings";

// DOM Elements mapped directly from index.html
const bookingForm = document.getElementById("bookingForm");
const bookingIdInput = document.getElementById("bookingId");
const customerNameInput = document.getElementById("customerName");
const movieNameInput = document.getElementById("movieName");
const showDateInput = document.getElementById("showDate");
const showTimeInput = document.getElementById("showTime");
const seatNumberInput = document.getElementById("seatNumber");
const submitBtn = document.getElementById("submitBtn");
const cancelEditBtn = document.getElementById("cancelEditBtn");
const bookingList = document.getElementById("bookingList");

// Load tickets on page load
document.addEventListener("DOMContentLoaded", fetchBookings);

// 1. Fetch all bookings (GET)
function fetchBookings() {
    fetch(API_URL, {
        method: "GET",
        credentials: "include"
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to load tickets");
        }
        return response.json();
    })
    .then(data => {
        renderTickets(data);
    })
    .catch(error => {
        console.error("Error fetching bookings:", error);
    });
}

// 2. Add or Update ticket (POST / PUT)
if (bookingForm) {
    bookingForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const bookingId = bookingIdInput.value;
        const bookingData = {
            customerName: customerNameInput.value,
            movieName: movieNameInput.value,
            showDate: showDateInput.value,
            showTime: showTimeInput.value,
            seatNumber: seatNumberInput.value
        };

        const isEdit = bookingId !== "";
        const targetUrl = isEdit ? `${API_URL}/${bookingId}` : API_URL;
        const httpMethod = isEdit ? "PUT" : "POST";

        fetch(targetUrl, {
            method: httpMethod,
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "include",
            body: JSON.stringify(bookingData)
        })
        .then(response => response.json())
        .then(data => {
            alert(data.message);
            if (data.success) {
                resetForm();
                fetchBookings();
            }
        })
        .catch(error => {
            console.error("Error saving booking:", error);
            alert("Failed to process request. Please check console.");
        });
    });
}

// 3. Render tickets
function renderTickets(tickets) {
    if (!bookingList) return;

    bookingList.innerHTML = "";

    if (!tickets || tickets.length === 0) {
        bookingList.innerHTML = "<p>No tickets booked yet.</p>";
        return;
    }

    tickets.forEach(ticket => {
        const ticketCard = document.createElement("div");
        ticketCard.className = "ticket-card";

        ticketCard.innerHTML = `
            <div class="ticket-info">
                <h4>${ticket.movieName}</h4>
                <p>Customer: ${ticket.customerName}</p>
                <p>Show: ${ticket.showDate} | ${ticket.showTime}</p>
                <span class="badge">Seat: ${ticket.seatNumber}</span>
            </div>
            <div class="card-actions">
                <button type="button" class="btn-edit" onclick="startEdit(${ticket.id}, '${escapeQuotes(ticket.customerName)}', '${escapeQuotes(ticket.movieName)}', '${ticket.showDate}', '${escapeQuotes(ticket.showTime)}', '${escapeQuotes(ticket.seatNumber)}')">Edit</button>
                <button type="button" class="btn-delete" onclick="deleteTicket(${ticket.id})">Delete</button>
            </div>
        `;

        bookingList.appendChild(ticketCard);
    });
}

function escapeQuotes(str) {
    return (str || "").replace(/'/g, "\\'");
}

// 4. Start edit: checks limit & begins 2-minute timer on backend
function startEdit(id, customerName, movieName, showDate, showTime, seatNumber) {
    fetch(`${API_URL}/${id}/start-edit`, {
        method: "POST",
        credentials: "include"
    })
    .then(res => res.json())
    .then(data => {
        if (!data.allowed) {
            alert(data.message);
            return;
        }

        bookingIdInput.value = id;
        customerNameInput.value = customerName;
        movieNameInput.value = movieName;
        showDateInput.value = showDate;
        showTimeInput.value = showTime;
        seatNumberInput.value = seatNumber;

        submitBtn.textContent = "Update Ticket";
        cancelEditBtn.style.display = "inline-block";
    })
    .catch(err => {
        console.error("Error starting edit:", err);
    });
}

if (cancelEditBtn) {
    cancelEditBtn.addEventListener("click", resetForm);
}

function resetForm() {
    bookingForm.reset();
    bookingIdInput.value = "";
    submitBtn.textContent = "Book Ticket Now";
    cancelEditBtn.style.display = "none";
}

// 5. Delete ticket (DELETE)
function deleteTicket(id) {
    if (confirm("Are you sure you want to delete this ticket?")) {
        fetch(`${API_URL}/${id}`, {
            method: "DELETE",
            credentials: "include"
        })
        .then(response => {
            if (response.ok) {
                alert("Ticket deleted successfully!");
                fetchBookings();
            } else {
                alert("Failed to delete ticket.");
            }
        })
        .catch(error => {
            console.error("Error deleting ticket:", error);
        });
    }
}