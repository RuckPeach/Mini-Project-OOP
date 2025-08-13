document.addEventListener('DOMContentLoaded', function() {
    const CLEANING_DURATION_MS = 5 * 60 * 1000; // 5 minutes in milliseconds

    function formatTime(milliseconds) {
        const totalSeconds = Math.floor(milliseconds / 1000);
        const minutes = Math.floor(totalSeconds / 60);
        const seconds = totalSeconds % 60;
        return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    }

    function updateTimes() {
        const now = Date.now();

        // Update Queue Wait Times
        document.querySelectorAll('.queue-item').forEach(item => {
            const joinTime = parseInt(item.dataset.joinTime);
            if (joinTime) {
                const elapsedMs = now - joinTime;
                const minutes = Math.floor(elapsedMs / (60 * 1000));
                item.querySelector('.wait-time').textContent = `รอ ${minutes} นาที`;
            }
        });

        // Update Table Times
        document.querySelectorAll('.table-card').forEach(tableCard => {
            const status = tableCard.dataset.status;
            const startTime = parseInt(tableCard.dataset.startTime);
            const tableTimeSpan = tableCard.querySelector('.table-time');
            const tableId = tableCard.dataset.tableId;

            if (status === 'OCCUPIED' && startTime) {
                const elapsedMs = now - startTime;
                const minutes = Math.floor(elapsedMs / (60 * 1000));
                tableTimeSpan.textContent = `${minutes} นาที`;
            } else if (status === 'CLEANING' && startTime) {
                const elapsedMs = now - startTime;
                const remainingMs = CLEANING_DURATION_MS - elapsedMs;

                if (remainingMs <= 0) {
                    tableTimeSpan.textContent = '00:00';
                    // Send request to server to free the table
                    fetch('buffet', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: `action=finishCleaning&tableId=${tableId}`
                    })
                    .then(response => {
                        if (response.ok) {
                            console.log(`Table ${tableId} automatically made available.`);
                            // Optionally, refresh the page or update UI dynamically
                            window.location.reload(); // For simplicity, reload the page
                        } else {
                            console.error(`Failed to free table ${tableId}.`);
                        }
                    })
                    .catch(error => console.error('Error:', error));

                } else {
                    tableTimeSpan.textContent = formatTime(remainingMs);
                }
            } else {
                tableTimeSpan.textContent = ''; // Clear time for AVAILABLE tables
            }
        });
    }

    // Update times every second for cleaning countdown accuracy
    setInterval(updateTimes, 1000);

    // Initial update
    updateTimes();
});