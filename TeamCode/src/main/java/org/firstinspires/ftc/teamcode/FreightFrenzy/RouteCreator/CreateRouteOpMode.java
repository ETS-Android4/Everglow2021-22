package org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator;

import static org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AutonomousRoute.CarouselPlaceFreightInstruction;
import static org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AutonomousRoute.CraterPlaceFreightInstruction;
import static org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AutonomousRoute.DeployDuckInstruction;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AutonomousRoute.DriveSidewaysInstruction;
import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AutonomousRoute.DriveStraightInstruction;
import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AutonomousRoute.PlaceFreightInstruction;
import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AutonomousRoute.ReloadArmInstruction;
import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AutonomousRoute.RouteInstruction;
import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AutonomousRoute.TurnInstruction;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

@TeleOp(name = "Create Route", group = "Linear Opmode")
public class CreateRouteOpMode extends LinearOpMode {

    private static final double DRIVE_SIDEWAYS_POWER = 0.4;
    private static final double DEFAULT_DRIVE_STRAIGHT_POWER = 0.6;
    private static final double STRONG_DRIVE_STRAIGHT_POWER = 1;
    private static final int ROTATE_SPEED_DECREASE = 150;
    private static final int DRIVE_TO_OBSTACLE_DISTANCE = 60;
    private static final long DUCK_DURATION = 5000;
    private static final double DRIVE_TO_OBSTACLE_POWER = 1;
    private AllSystems systems;
    @Nullable
    private AutonomousRoute prevAutonomousRoute;
    private EverglowGamepad ourGamepad1;
    private EverglowGamepad ourGamepad2;

    @Override
    public void runOpMode() {
        systems = AllSystems.init(this);
        ourGamepad1 = new EverglowGamepad(gamepad1);
        ourGamepad2 = new EverglowGamepad(gamepad2);
        waitForStart();

        if (opModeIsActive()) {
            prevAutonomousRoute = recordAutonomousRoute();
            Utils.saveToClipBoard(prevAutonomousRoute.toJavaCode());
        }
        while (opModeIsActive()) {
            ourGamepad1.update();
            ourGamepad2.update();

            telemetry.addLine("Press cross to start recording route.");
            if (prevAutonomousRoute != null) {
                telemetry.addLine("Press square to replay previous route.");
                telemetry.addLine("Press circle to replay previous route mirrored.");
                telemetry.addLine();
                telemetry.addLine("Code: ");
                telemetry.addLine(prevAutonomousRoute.toJavaCode());

                if (ourGamepad2.x()) {
                    prevAutonomousRoute.execute(systems, 1);
                }
                if (ourGamepad2.b()){
                    // runs the autonomous mirrored
                    prevAutonomousRoute.execute(systems, -1);
                }
            }

            if (ourGamepad2.a()) {
                prevAutonomousRoute = recordAutonomousRoute();
                Utils.saveToClipBoard(prevAutonomousRoute.toJavaCode());
            }

            telemetry.update();
        }
    }

    private AutonomousRoute recordAutonomousRoute() {
        AutonomousRoute autonomousRoute = new AutonomousRoute();
        while (opModeIsActive()) {
            ourGamepad1.update();
            ourGamepad2.update();

            telemetry.addLine("Press square to stop recording. ");
            telemetry.addLine("Press triangle to drive forward strong");
            telemetry.addLine("Press circle to rotate 180 degrees");
            telemetry.addLine("Press cross to place freight now");
            telemetry.addLine("Press dpad right to reload arm");
            telemetry.addLine("Press dpad up to active duck system");
            telemetry.addLine("Press dpad left to place freight on caursel side");
            telemetry.addLine("Press dpad right to place freight on crater side");
            telemetry.addLine();
            telemetry.addLine("Current code: ");
            telemetry.addLine(autonomousRoute.toJavaCode());
            telemetry.update();

            if (ourGamepad2.x()) {
                return autonomousRoute;
            }

            if (gamepad2.left_stick_x != 0 || gamepad2.left_stick_y != 0 || gamepad2.right_stick_x != 0) {
                TimeUtils.sleep(50); // delay for a few milliseconds, to ensure the use action is correct
                ourGamepad1.update();
                ourGamepad2.update();
                // determine which direction is pressed the hardest, and record for that direction.
                double left_stick_x_power = Math.abs(gamepad2.left_stick_x);
                double left_stick_y_power = Math.abs(gamepad2.left_stick_y);
                double right_stick_x_power = Math.abs(gamepad2.right_stick_x);
                if (left_stick_x_power == 0 && left_stick_y_power == 0 && right_stick_x_power == 0) {
                    // the user has let go of the joystick
                } else if (left_stick_x_power > left_stick_y_power && left_stick_x_power > right_stick_x_power) {
                    RouteInstruction routeInstruction = recordDriveSideways();
                    autonomousRoute.addRouteInstruction(routeInstruction);
                } else if (left_stick_y_power > left_stick_x_power && left_stick_y_power > right_stick_x_power) {
                    RouteInstruction routeInstruction = recordDriveStraight();
                    autonomousRoute.addRouteInstruction(routeInstruction);
                } else {
                    RouteInstruction routeInstruction = recordTurn90();
                    autonomousRoute.addRouteInstruction(routeInstruction);
                }
            }

            if (ourGamepad2.b()){
                TurnInstruction turnInstruction = new TurnInstruction(180, ROTATE_SPEED_DECREASE);
                turnInstruction.execute(systems, 1);
                autonomousRoute.addRouteInstruction(turnInstruction);
            }

            if (ourGamepad2.y()) {
                RouteInstruction routeInstruction = recordStrongDriveStraight();
                autonomousRoute.addRouteInstruction(routeInstruction);
            }

            if (ourGamepad2.a()){
                RouteInstruction routeInstruction = new PlaceFreightInstruction();
                routeInstruction.execute(systems, 1);
                autonomousRoute.addRouteInstruction(routeInstruction);
            }

            if (ourGamepad2.dpad_up()) {
                RouteInstruction routeInstruction = new DeployDuckInstruction(DUCK_DURATION);
                routeInstruction.execute(systems, 1);
                autonomousRoute.addRouteInstruction(routeInstruction);
            }

            if (ourGamepad2.dpad_left()) {
                RouteInstruction routeInstruction = new CarouselPlaceFreightInstruction();
                routeInstruction.execute(systems, 1);
                autonomousRoute.addRouteInstruction(routeInstruction);
            }

            if (ourGamepad2.dpad_right()) {
                RouteInstruction routeInstruction = new CraterPlaceFreightInstruction();
                routeInstruction.execute(systems, 1);
                autonomousRoute.addRouteInstruction(routeInstruction);
            }

            if (ourGamepad2.dpad_down()){
                RouteInstruction routeInstruction = new ReloadArmInstruction();
                routeInstruction.execute(systems, 1);
                autonomousRoute.addRouteInstruction(routeInstruction);
            }
        }
        // the code should only reach this point if the opMode ends abrupty, in which case this value won't be used.
        return autonomousRoute;
    }

    private RouteInstruction recordDriveSideways() {
        if (gamepad2.left_stick_x > 0) {
            // drive to the right
            double distanceDriven = systems.drivingSystem.driveSidewaysUntil(DRIVE_SIDEWAYS_POWER,
                    () -> gamepad2.left_stick_x <= 0
            );

            return new DriveSidewaysInstruction(DRIVE_SIDEWAYS_POWER, distanceDriven);
        } else {
            // drive to the left
            double distanceDriven = systems.drivingSystem.driveSidewaysUntil(-DRIVE_SIDEWAYS_POWER,
                    () -> gamepad2.left_stick_x >= 0
            );
            return new DriveSidewaysInstruction(-DRIVE_SIDEWAYS_POWER, distanceDriven);
        }
    }

    private RouteInstruction recordDriveStraight() {
        if (gamepad2.left_stick_y > 0) {
            // drive backwards
            double distanceDriven = systems.drivingSystem.driveStraightUntil(-DEFAULT_DRIVE_STRAIGHT_POWER,
                    () -> gamepad2.left_stick_y <= 0
            );

            return new DriveStraightInstruction(-DEFAULT_DRIVE_STRAIGHT_POWER, distanceDriven);
        } else {
            // drive forwards
            double distanceDriven = systems.drivingSystem.driveStraightUntil(DEFAULT_DRIVE_STRAIGHT_POWER,
                    () -> gamepad2.left_stick_y >= 0
            );
            return new DriveStraightInstruction(DEFAULT_DRIVE_STRAIGHT_POWER, distanceDriven);
        }
    }

    private RouteInstruction recordStrongDriveStraight(){
        double distanceDriven = systems.drivingSystem.driveStraightUntil(STRONG_DRIVE_STRAIGHT_POWER,
                () -> gamepad2.y
        );
        return new DriveStraightInstruction(DEFAULT_DRIVE_STRAIGHT_POWER, distanceDriven);
    }

    private RouteInstruction recordTurn90() {
        float rotateAngle = gamepad2.right_stick_x > 0 ? 90 : -90;
        TurnInstruction turnInstruction = new TurnInstruction(rotateAngle, ROTATE_SPEED_DECREASE);
        turnInstruction.execute(systems, 1);
        return turnInstruction;
    }

}
