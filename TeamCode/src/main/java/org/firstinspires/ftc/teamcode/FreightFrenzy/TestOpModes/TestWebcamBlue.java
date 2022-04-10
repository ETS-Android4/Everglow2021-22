package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;

@TeleOp(name = "Test Webcam Blue", group = "Test Opmode")
public class TestWebcamBlue extends TestWebcamBase{
    public TestWebcamBlue() {
        super(MathUtils.Side.BLUE);
    }
}
