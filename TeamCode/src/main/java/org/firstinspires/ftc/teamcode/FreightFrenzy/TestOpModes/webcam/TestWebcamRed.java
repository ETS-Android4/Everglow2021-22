package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes.webcam;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;

@TeleOp(name = "Test Webcam Red", group = "Webcam")
public class TestWebcamRed extends TestWebcamBase{
    public TestWebcamRed() {
        super(MathUtils.Side.RED);
    }
}
