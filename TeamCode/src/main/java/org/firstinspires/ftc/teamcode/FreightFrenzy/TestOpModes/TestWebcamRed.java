package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;

@TeleOp(name = "Test Webcam Red", group = "Test Opmode")
public class TestWebcamRed extends TestWebcamBase{
    public TestWebcamRed() {
        super(MathUtils.Side.RED);
    }
}
