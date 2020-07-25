<%@ page import="il.ac.hit.costmanagement.dm.User" %>
<%@ page import="il.ac.hit.costmanagement.exception.CostManagementException" %>
<%@ page import="il.ac.hit.costmanagement.model.CostManagementDAO" %>
<%@ page import="il.ac.hit.costmanagement.model.IIncomingDAO" %>
<%@ page import="il.ac.hit.costmanagement.model.ISpendDAO" %>
<%--
  Created by IntelliJ IDEA.
  User: Maor
  Date: 24/07/2020
  Time: 17:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    private ISpendDAO spendDAO = CostManagementDAO.getInstance();
    private IIncomingDAO incomingDAO = CostManagementDAO.getInstance();
    private User user;

    private double shopping = 0;
    private double transport = 0;
    private double restaurant = 0;
    private double health = 0;
    private  double family = 0;
    private double groceries = 0;
    private double leisure = 0;
    private double government = 0;
    private  double food = 0;

    private double januarySpend = 0;
    private double februarySpend = 0;
    private double marchSpend = 0;
    private double aprilSpend = 0;
    private double maySpend = 0;
    private double juneSpend = 0;
    private double julySpend = 0;
    private double augustSpend = 0;
    private double septemberSpend = 0;
    private double octoberSpend = 0;
    private double novemberSpend = 0;
    private double decemberSpend = 0;


    double januaryIncome = 0;
    double februaryIncome = 0;
    double marchIncome = 0;
    double aprilIncome = 0;
    double mayIncome = 0;
    double juneIncome = 0;
    double julyIncome = 0;
    double augustIncome = 0;
    double septemberIncome = 0;
    double octoberIncome = 0;
    double novemberIncome = 0;
    double decemberIncome = 0;

%>

<%
    user = (User)request.getAttribute("currentUser");

    try {
        getAllCategories();
        getAllYearCosts();
    } catch (CostManagementException e) {
        e.printStackTrace();
    }

%>



<html>
<head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {

            var data = google.visualization.arrayToDataTable([
                ['Task', 'Hours per Day'],
                ['Shopping',     <%=shopping%>],
                ['Transport',   <%=transport%>],
                ['Restaurant TV',  <%=restaurant%>],
                ['Health',    <%=health%>],
                ['Family',    <%=family%>],
                ['Groceries',    <%=groceries%>],
                ['Leisure',    <%=leisure%>],
                ['Government',    <%=government%>],
                ['Food',    <%=food%>]
            ]);

            var options = {
                title: 'My Monthly costs',
                is3D : true,
            };


            var chart = new google.visualization.PieChart(document.getElementById('piechart'));

            chart.draw(data, options);

        }
    </script>
</head>
<body>
<div id="piechart" style="width: 900px; height: 500px;"></div>
</body>
</html>


<html>
<head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {
            var data = google.visualization.arrayToDataTable([
                ['Month', 'Incoming', 'Spend'],
                ['January',  <%=januaryIncome%>,      <%=januarySpend%>],
                ['February',  <%=februaryIncome%>,      <%=februarySpend%>],
                ['March',  <%=marchIncome%>,       <%=marchSpend%>],
                ['April',  <%=aprilIncome%>,      <%=aprilSpend%>],
                ['May',  <%=mayIncome%>,       <%=maySpend%>],
                ['June',  <%=juneIncome%>,       <%=juneSpend%>],
                ['July',  <%=julyIncome%>,       <%=julySpend%>],
                ['August',  <%=augustIncome%>,       <%=augustSpend%>],
                ['September',  <%=septemberIncome%>,       <%=septemberSpend%>],
                ['October',  <%=octoberIncome%>,       <%=octoberSpend%>],
                ['November',  <%=novemberIncome%>,       <%=novemberSpend%>],
                ['December',  <%=decemberIncome%>,       <%=decemberSpend%>],
            ]);

            var options = {
                title: 'My year cost',
                curveType: 'function',
                legend: { position: 'bottom' }
            };

            var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

            chart.draw(data, options);
        }
    </script>
</head>
<body>
<div id="curve_chart" style="width: 1000px; height: 400px"></div>
</body>
</html>




<%!
    private void getAllYearCosts() throws CostManagementException {
        januarySpend = spendDAO.getSpendByMonth(user.getId(),1);
        februarySpend = spendDAO.getSpendByMonth(user.getId(),2);
        marchSpend = spendDAO.getSpendByMonth(user.getId(),3);
        aprilSpend = spendDAO.getSpendByMonth(user.getId(),4);
        maySpend = spendDAO.getSpendByMonth(user.getId(),5);
        juneSpend = spendDAO.getSpendByMonth(user.getId(),6);
        julySpend = spendDAO.getSpendByMonth(user.getId(),7);
        augustSpend = spendDAO.getSpendByMonth(user.getId(),8);
        septemberSpend = spendDAO.getSpendByMonth(user.getId(),9);
        octoberSpend = spendDAO.getSpendByMonth(user.getId(),10);
        novemberSpend = spendDAO.getSpendByMonth(user.getId(),11);
        decemberSpend = spendDAO.getSpendByMonth(user.getId(),12);

        januaryIncome = incomingDAO.getIncomeByMonth(user.getId(),1);
        februaryIncome = incomingDAO.getIncomeByMonth(user.getId(),2);
        marchIncome = incomingDAO.getIncomeByMonth(user.getId(),3);
        aprilIncome = incomingDAO.getIncomeByMonth(user.getId(),4);
        mayIncome = incomingDAO.getIncomeByMonth(user.getId(),5);
        juneIncome = incomingDAO.getIncomeByMonth(user.getId(),6);
        julyIncome = incomingDAO.getIncomeByMonth(user.getId(),7);
        augustIncome = incomingDAO.getIncomeByMonth(user.getId(),8);
        septemberIncome =incomingDAO.getIncomeByMonth(user.getId(),9);
        octoberIncome = incomingDAO.getIncomeByMonth(user.getId(),10);
        novemberIncome = incomingDAO.getIncomeByMonth(user.getId(),11);
        decemberIncome = incomingDAO.getIncomeByMonth(user.getId(),12);

    }

    public void getAllCategories() throws CostManagementException {
        shopping = spendDAO.getSpendByCategory(user,"Shopping");
        transport = spendDAO.getSpendByCategory(user,"Transport");
        restaurant = spendDAO.getSpendByCategory(user,"Restaurant");
        health = spendDAO.getSpendByCategory(user,"Health");
        family = spendDAO.getSpendByCategory(user,"Family");
        groceries = spendDAO.getSpendByCategory(user,"Groceries");
        leisure = spendDAO.getSpendByCategory(user,"Leisure");
        government = spendDAO.getSpendByCategory(user,"Government");
        food = spendDAO.getSpendByCategory(user,"Food");
    }
%>



