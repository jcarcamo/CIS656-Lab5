package edu.gvsu.cis.cis656.dht;

/**
 * This class represents the information that the chat client registers
 * with the presence server.
 */

public class RegistrationInfo
{
	private String name;
    private String host;
    private boolean status;
    private int port;

    public RegistrationInfo()
    {

    }
    /**
     * RegistrationInfo  constructor.
     * @param uname Name of the user being registered.
     * @param h Name of the host their client is running on.
     * @param p The port # their client is listening for connections on.
     * @param s The status, true if the client is available, false otherwise.
     */
    public RegistrationInfo(String uname, String h, int p, boolean s)
    {
        this.name = uname;
        this.host = h;
        this.port = p;
        this.status = s;
    }

    /**
     * Determine the name of the user.
     * @return The name of the user.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Determine the host the user is on.
     * @return The name of the host client resides on.
     */
    public String getHost()
    {
        return this.host;
    }

    /**
     * Get the port the client is listening for connections on.
     * @return port value.
     */
    public int getPort()
    {
        return this.port;
    }

    /**
     * Get the status of the client - true means availability, false means don't disturb.
     * @return status value.
     */
    public boolean getStatus()
    {
    	return this.status;
    }

	/**
	 * @param userName the userName to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	public boolean equals(Object obj){
		boolean equality = false;
		if(this != null && obj != null && (obj instanceof RegistrationInfo) && ((RegistrationInfo) obj).getName().equals(this.getName())){
			equality = true;
		}
        return equality;
    }

	/**
	 * Convert this object to a string for representation
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("user-name:");
		sb.append(this.name);
		sb.append(",status:");
		sb.append(this.status);
		return sb.toString();
	}

	/**
	 * Convert this object into an HTML representation.
	 * @param fragment if true, generate an html fragment, otherwise a complete document.
	 * @return an HTML representation.
	 */
	public String toHtml(boolean fragment)
	{
		String retval = "";
		if(fragment) {
			StringBuffer sb = new StringBuffer();
			sb.append("<b>User Name:</b> ");
			sb.append(this.name);
			sb.append("<b> Status: </b>");
			sb.append(this.status);
			sb.append(" <a href=\"/v1/users/" + this.name + "\">View</a>");
			sb.append("<br/>");
			retval = sb.toString();
		} else {
			StringBuffer sb = new StringBuffer("<html><head><title>Lab4 - User</title></head><body><h1>Lab4 - User</h1>");
			sb.append("<b>Name: </b> ");
			sb.append(this.name);
			sb.append("<br/><b>Network: </b>");
			sb.append(this.host).append(":").append(this.port);
			sb.append("<br/><b>Status:</b> ");
			sb.append(this.status);
			sb.append("<br/><br/>Return to <a href=\"/v1/users\">users list<a>.</body></html>");
			retval = sb.toString();
		}
		return retval;
	}

}
