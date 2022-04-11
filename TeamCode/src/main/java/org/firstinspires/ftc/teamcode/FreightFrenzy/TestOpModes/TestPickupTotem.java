package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.Routes;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

@TeleOp(name = "Test Pickup Totem", group = "Test")
public class TestPickupTotem extends LinearOpMode {
    Routes routes;
    AllSystems allSystems;

    @Override
    public void runOpMode() {
        allSystems = new AllSystems(this, MathUtils.Side.RED);
        routes = new Routes(allSystems);

        waitForStart();

        routes.pickupTotem();
//        allSystems.drivingSystem.driveStraight(-20, 0.5);
    }
}
