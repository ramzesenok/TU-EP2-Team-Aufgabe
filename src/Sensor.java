public class Sensor {
    private String id;
    private int originallyOverallRequests;
    private int overallRequests, requestsSinceLastDivision;

    public Sensor(String listCell) {
        int firstSemicolonIndex = listCell.indexOf(";");
        int lastSemicolonIndex = listCell.lastIndexOf(";");

        if (firstSemicolonIndex != -1 && lastSemicolonIndex != -1) {
            String id = listCell.substring(0, firstSemicolonIndex);
            int overallRequests = Integer.parseInt(listCell.substring(firstSemicolonIndex + 1, lastSemicolonIndex));
            int requestsSinceLastDivision = Integer.parseInt(listCell.substring(lastSemicolonIndex + 1));

            this.id = id;
            this.overallRequests = overallRequests;
            this.originallyOverallRequests = overallRequests;
            this.requestsSinceLastDivision = requestsSinceLastDivision;
        }
    }

    public void makeRequest() {
        overallRequests++;
        requestsSinceLastDivision++;
    }

    public String getId() {
        return id;
    }

    private int getOverallRequests() {
        return overallRequests;
    }

    public int getRequestsSinceLastDivision() {
        return requestsSinceLastDivision;
    }

    public void setRequestsSinceLastDivisionToZero() {
        requestsSinceLastDivision = 0;
    }

    public void setOriginallyOverallRequestsToCurrentOverallRequestsCount() {
        originallyOverallRequests = overallRequests;
    }

    public boolean reachedOveralRequestsLimit() { return overallRequests >= 1000; }

    public boolean reachedRequestsSinceLastDivisionLimit() { return requestsSinceLastDivision >= 250; }

    public boolean didNotGetAnyRequests() {
        return originallyOverallRequests == overallRequests;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + ", Overal requests: " + getOverallRequests() + ", Requests since last division: " + getRequestsSinceLastDivision();
    }
}
