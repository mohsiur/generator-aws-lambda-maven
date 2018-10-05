# <%=projectName%>

The following is a lambda for <%=applicationName%> and is invoked by <%if(invokerAPIGateway){%>API Gateway<%}%><%if(invokerSNS){%>SNS Event<%}%><%if(invokerDefault){%>any given Object<%}%>

## Pre-requisites

To deploy into AWS a few pre-requisites need to be met

* [AWS cli](https://aws.amazon.com/cli/) installed and configured with your credentials
