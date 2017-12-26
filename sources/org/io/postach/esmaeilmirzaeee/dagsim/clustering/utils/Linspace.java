package org.io.postach.esmaeilmirzaeee.dagsim.clustering.utils;

/**
 * Created by esmaeil on 7/29/17.
 * def linspace(start, stop, num=50, endpoint=True, retstep=False, dtype=None):
 """
 Return evenly spaced numbers over a specified interval.
 Returns `num` evenly spaced samples, calculated over the
 interval [`start`, `stop`].
 The endpoint of the interval can optionally be excluded.
 Parameters
 ----------
 start : scalar
 The starting value of the sequence.
 stop : scalar
 The end value of the sequence, unless `endpoint` is set to False.
 In that case, the sequence consists of all but the last of ``num + 1``
 evenly spaced samples, so that `stop` is excluded.  Note that the step
 size changes when `endpoint` is False.
 num : int, optional
 Number of samples to generate. Default is 50. Must be non-negative.
 endpoint : bool, optional
 If True, `stop` is the last sample. Otherwise, it is not included.
 Default is True.
 retstep : bool, optional
 If True, return (`samples`, `step`), where `step` is the spacing
 between samples.
 dtype : dtype, optional
 The type of the output array.  If `dtype` is not given, infer the data
 type from the other input arguments.
 .. versionadded:: 1.9.0
 Returns
 -------
 samples : ndarray
 There are `num` equally spaced samples in the closed interval
 ``[start, stop]`` or the half-open interval ``[start, stop)``
 (depending on whether `endpoint` is True or False).
 step : float, optional
 Only returned if `retstep` is True
 Size of spacing between samples.
 See Also
 --------
 arange : Similar to `linspace`, but uses a step size (instead of the
 number of samples).
 logspace : Samples uniformly distributed in log space.
 Examples
 --------
 >>> np.linspace(2.0, 3.0, num=5)
 array([ 2.  ,  2.25,  2.5 ,  2.75,  3.  ])
 >>> np.linspace(2.0, 3.0, num=5, endpoint=False)
 array([ 2. ,  2.2,  2.4,  2.6,  2.8])
 >>> np.linspace(2.0, 3.0, num=5, retstep=True)
 (array([ 2.  ,  2.25,  2.5 ,  2.75,  3.  ]), 0.25)
 Graphical illustration:
 >>> import matplotlib.pyplot as plt
 >>> N = 8
 >>> y = np.zeros(N)
 >>> x1 = np.linspace(0, 10, N, endpoint=True)
 >>> x2 = np.linspace(0, 10, N, endpoint=False)
 >>> plt.plot(x1, y, 'o')
 [<matplotlib.lines.Line2D object at 0x...>]
 >>> plt.plot(x2, y + 0.5, 'o')
 [<matplotlib.lines.Line2D object at 0x...>]
 >>> plt.ylim([-0.5, 1])
 (-0.5, 1)
 >>> plt.show()
 """
 # 2016-02-25, 1.12
 num = _index_deprecate(num)
 if num < 0:
 raise ValueError("Number of samples, %s, must be non-negative." % num)
 div = (num - 1) if endpoint else num

 # Convert float/complex array scalars to float, gh-3504
 # and make sure one can use variables that have an __array_interface__, gh-6634
 start = asanyarray(start) * 1.0
 stop  = asanyarray(stop)  * 1.0

 dt = result_type(start, stop, float(num))
 if dtype is None:
 dtype = dt

 y = _nx.arange(0, num, dtype=dt)

 delta = stop - start
 if num > 1:
 step = delta / div
 if step == 0:
 # Special handling for denormal numbers, gh-5437
 y /= div
 y = y * delta
 else:
 # One might be tempted to use faster, in-place multiplication here,
 # but this prevents step from overriding what class is produced,
 # and thus prevents, e.g., use of Quantities; see gh-7142.
 y = y * step
 else:
 # 0 and 1 item long sequences have an undefined step
 step = NaN
 # Multiply with delta to allow possible override of output class.
 y = y * delta

 y += start

 if endpoint and num > 1:
 y[-1] = stop

 if retstep:
 return y.astype(dtype, copy=False), step
 else:
 return y.astype(dtype, copy=False)
 */
public class Linspace {
    public static double[] linspace(double min, double max, int points) {
        double[] d = new double[points];
        for (int i = 0; i < points; i++){
            d[i] = min + i * (max - min) / (points - 1);
        }
        return d;
    }
}
