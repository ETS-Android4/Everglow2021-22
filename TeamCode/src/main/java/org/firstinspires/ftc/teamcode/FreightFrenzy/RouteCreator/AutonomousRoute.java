package org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator;

import java.util.ArrayList;
import java.util.List;

public class AutonomousRoute {
    interface RouteInstruction {
        void execute(AllSystems systems);
        String toJavaCode();
    }

    static class DriveStraightInstruction implements RouteInstruction{
        private final double power;
        private final double distance;

        public DriveStraightInstruction(double power, double distance) {
            this.power = power;
            this.distance = distance;
        }

        @Override
        public void execute(AllSystems systems) {
            systems.drivingSystem.driveStraight(distance, power);
        }

        @Override
        public String toJavaCode() {
            return String.format("systems.drivingSystem.driveStraight(%f, %f)", distance, power);
        }
    }

    static class DriveSidewaysInstruction implements RouteInstruction{
        private final double power;
        private final double distance;

        public DriveSidewaysInstruction(double power, double distance) {
            this.power = power;
            this.distance = distance;
        }

        @Override
        public void execute(AllSystems systems) {
            systems.drivingSystem.driveSideways(distance, power);
        }

        @Override
        public String toJavaCode() {
            return String.format("systems.drivingSystem.driveSideways(%f, %f)", distance, power);
        }
    }

    static class TurnInstruction implements RouteInstruction{
        private final double degrees;
        private final int speedDecrease;

        public TurnInstruction(double degrees, int speedDecrease) {
            this.degrees = degrees;
            this.speedDecrease = speedDecrease;
        }

        @Override
        public void execute(AllSystems systems) {
            systems.drivingSystem.driveSideways(degrees, speedDecrease);
        }

        @Override
        public String toJavaCode() {
            return String.format("systems.drivingSystem.turn(%f, %f)", degrees, speedDecrease);
        }
    }






    private final List<RouteInstruction> routeInstructions = new ArrayList<>();

    public void execute(AllSystems systems){
        for (RouteInstruction routeInstruction : this.routeInstructions){
            routeInstruction.execute(systems);
        }
    }

    public String toJavaCode(){
        String methodName = "autonomous_generated";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("public void %s(AllSystems systems){\n", methodName));
        for (RouteInstruction routeInstruction : this.routeInstructions){
            sb.append(routeInstruction.toJavaCode());
        }
        sb.append("}");
        return sb.toString();
    }

    public void addRouteInstruction(RouteInstruction routeInstruction){
        routeInstructions.add(routeInstruction);
    }
}
